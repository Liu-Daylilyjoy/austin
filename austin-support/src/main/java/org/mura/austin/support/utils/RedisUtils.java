package org.mura.austin.support.utils;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/3 11:37
 *
 * 操作Redis
 */
@Component
@Slf4j
public class RedisUtils {
    private StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * mGet将结果封装为Map
     */
    public Map<String, String> mGet(List<String> keys) {
        HashMap<String, String> result = new HashMap<>(keys.size());

        try {
            List<String> value = redisTemplate.opsForValue().multiGet(keys);

            if (CollUtil.isNotEmpty(value)) {
                for (int i = 0; i < keys.size(); i++) {
                    result.put(keys.get(i), value.get(i));
                }
            }
        } catch (Exception e) {
            log.error("redis mGet fail! e:{}", Throwables.getStackTraceAsString(e));
        }

        return result;
    }

    /**
     * pipeline 设置 key-value 并设置过期时间，批量设置多个key
     */
    public void pipelineSetEx(Map<String, String> keyValues, Long seconds) {
        try {
            redisTemplate.executePipelined((RedisCallback<String>) connection -> {
                for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                    connection.setEx(entry.getKey().getBytes(), seconds,
                            entry.getValue().getBytes());
                }

                return null;
            });
        } catch (Exception e) {
            log.error("redis pipelineSetEX fail! e:{}", Throwables.getStackTraceAsString(e));
        }
    }
}
