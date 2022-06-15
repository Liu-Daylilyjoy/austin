package org.mura.austin.service.deduplication.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.constant.AustinConstant;
import org.mura.austin.domain.AnchorInfo;
import org.mura.austin.domain.DeduplicationParam;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.service.deduplication.DeduplicationHolder;
import org.mura.austin.utils.LogUtils;
import org.mura.austin.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/3 11:34
 *
 * 去重服务模板类
 *
 * 消息发送平台只能把共性的去重功能给做掉，例如5分钟内去重同一用户收到的相同消息
 * 跟业务强挂钩的消息去重应由业务方自行实现，因此使用了模板设计模式
 */
@Slf4j
public abstract class AbstractDeduplicationService implements DeduplicationService {
    protected Integer deduplicationType;

    @Autowired
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    private void init() {
        deduplicationHolder.putService(deduplicationType, this);
    }

    private RedisUtils redisUtils;

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 去重
     */
    @Override
    public void deduplication(DeduplicationParam param) {
        TaskInfo taskInfo = param.getTaskInfo();
//        需要过滤的用户集合
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
//        获取redis记录
        Set<String> readyPutRedisReceiver = new HashSet<>(taskInfo.getReceiver().size());
//        根据taskInfo获取其中所有receiver去重所依赖的key，key是唯一的，key可能不是receiver
        List<String> keys = getAllDeduplicationKeys(taskInfo);

//        从redis查询key，如果查询到了说明不久前出现过这条消息，（可能）需要去重
//        key的值存储的是出现次数，可以在DeduplicationParam中设置消息的重复阈值
        Map<String, String> inRedisValue = redisUtils.mGet(keys);

        for (String receiver : taskInfo.getReceiver()) {
//            再次构建去重key，这次是为了获取redis中的value（存储key出现的次数）
            String key = getDeduplicationKey(taskInfo, receiver);
            String value = inRedisValue.get(key);

//            判断符合去重条件的用户，出现次数大于等于去重阈值的进行去重
//            如果是对重复消息进行去重的功能，则将redis中的值设置为1即可
//            两种去重方式在redis中存储的key是不相同的，互不冲突
//            换言之，内容去重看重的是content，频次去重看重的是sendChannel
            if (value != null && Integer.parseInt(value) >= param.getCountNum()) {
                filterReceiver.add(receiver);
            } else {
//                否则加入redis，redis中已存在的则将计数+1
                readyPutRedisReceiver.add(receiver);
            }
        }

        // 不符合条件的用户：需要更新Redis(无记录添加，有记录则累加次数)
        putInRedis(readyPutRedisReceiver, inRedisValue, param);

        // 剔除符合去重条件的用户（频次去重）
        if (CollUtil.isNotEmpty(filterReceiver)) {
            taskInfo.getReceiver().removeAll(filterReceiver);
            LogUtils.print(AnchorInfo.builder()
                    .businessId(taskInfo.getBusinessId())
                    .ids(filterReceiver)
                    .state(param.getAnchorState().getCode())
                    .build());
        }
    }

    /**
     * 构建去重的Key，由具体的业务方实现
     */
    protected abstract String getDeduplicationKey(TaskInfo taskInfo, String receiver);


    /**
     * 存入redis 实现去重
     */
    private void putInRedis(Set<String> readyPutRedisReceiver,
                            Map<String, String> inRedisValue,
                            DeduplicationParam param) {
        Map<String, String> keyValues = new HashMap<>(readyPutRedisReceiver.size());

        for (String receiver : readyPutRedisReceiver) {
//            再次构建去重key，这次也是为了获取redis中的value
//            #TODO 后续可能优化，相同操作太多而且有并发问题
            String key = getDeduplicationKey(param.getTaskInfo(), receiver);

            if (inRedisValue.get(key) != null) {
                keyValues.put(key, String.valueOf(Integer.parseInt(inRedisValue.get(key)) + 1));
            } else {
                keyValues.put(key, String.valueOf(AustinConstant.TRUE));
            }
        }

//        设置过期时间
        if (CollUtil.isNotEmpty(keyValues)) {
            redisUtils.pipelineSetEx(keyValues, param.getDeduplicationTime());
        }
    }

    /**
     * 获取得到当前消息中所有的receiver去重所依赖的key
     */
    private List<String> getAllDeduplicationKeys(TaskInfo taskInfo) {
        List<String> result = new ArrayList<>(taskInfo.getReceiver().size());

        for (String receiver : taskInfo.getReceiver()) {
//            根据taskInfo和特定的receiver（一般只有个别receiver会重复），进行特定业务的消息去重
            String key = getDeduplicationKey(taskInfo, receiver);
            result.add(key);
        }

        return result;
    }
}
