package org.mura.austin.utils;

import cn.hutool.core.date.DateUtil;
import org.mura.austin.constant.AustinConstant;

import java.util.Date;

/**
 * @author Akutagawa Murasame
 */
public class TaskInfoUtils {
    private static final int TYPE_FLAG = 1000000;

    /**
     * 生成BusinessId
     * 模板类型 * TYPE_FLAG + 模板ID（主键）+ 当天日期 比如1000000120220424
     */
    public static Long generateBusinessId(Long templateId, Integer templateType) {
        Integer today = Integer.valueOf(DateUtil.format(new Date(), AustinConstant.YYYYMMDD));
        return Long.valueOf(String.format("%d%s", (long) templateType * TYPE_FLAG + templateId, today));
    }

    /**
     * 对url添加平台参数（用于追踪参数）
     */
    public static String generateUrl(String url, Long templateId, Integer templateType) {
        url = url.trim();
        Long businessId = generateBusinessId(templateId, templateType);

        if (url.indexOf('?') == -1) {
            return url + "?track_code_bid=" + businessId;
        } else {
            return url + "&track_code_bid=" + businessId;
        }
    }
}