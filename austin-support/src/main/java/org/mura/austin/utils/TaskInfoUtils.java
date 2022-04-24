package org.mura.austin.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author Akutagawa Murasame
 */
public class TaskInfoUtils {
    private static int TYPE_FLAG = 1000000;

    /**
     * 生成BusinessId
     * 模板类型 * TYPE_FLAG + 模板ID（主键）+ 当天日期 比如1000000120220424
     */
    public static Long generateBusinessId(Long templateId, Integer templateType) {
        Integer today = Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd"));
        return Long.valueOf(String.format("%d%s", (long) templateType * TYPE_FLAG + templateId, today));
    }
}