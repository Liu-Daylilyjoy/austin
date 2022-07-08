package org.mura.austin.support.utils;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.CustomLogListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.common.domain.AnchorInfo;
import org.mura.austin.common.domain.LogParam;
import org.springframework.stereotype.Component;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/8 15:58
 *
 * 自定义Log工具类
 *
 * 框架会搜寻容器中所有CustomLog类型的bean，并且代替默认bean
 */
@Slf4j
@Component
public class LogUtils extends CustomLogListener {
    /**
     * 方法切面的日志 @OperationLog 所产生
     */
    @Override
    public void createLog(LogDTO logDTO) throws Exception {
        log.info(JSON.toJSONString(logDTO));
    }

    /**
     * 记录当前对象信息
     */
    public static void print(LogParam logParam) {
        logParam.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(logParam));
    }

    /**
     * 记录锚点信息
     */
    public static void print(AnchorInfo anchorInfo) {
        anchorInfo.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(anchorInfo));
    }

    /**
     * 记录当前对象信息和锚点信息
     */
    public static void print(LogParam logParam,AnchorInfo anchorInfo) {
        print(anchorInfo);
        print(logParam);
    }
}
