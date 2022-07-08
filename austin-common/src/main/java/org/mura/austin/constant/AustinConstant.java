package org.mura.austin.constant;

/**
 * @author Akutagawa Murasame
 *
 * 基础的常量信息
 */
public class AustinConstant {
    /**
     * boolean转换
     */
    public final static Integer TRUE = 1;
    public final static Integer FALSE = 0;

    /**
     * 时间格式
     */
    public final static String YYYY_MM_DD = "yyyyMMdd";

    /**
     * cron时间格式
     *
     * 秒 分 时 天 月 星期 年(yyyy年到yyyy年执行)
     */
    public final static String CRON_FORMAT = "ss mm HH dd MM ? yyyy-yyyy";

    /**
     * Apollo常量默认值，即当无法获取目标配置时的默认值
     */
    public final static String APOLLO_DEFAULT_VALUE_JSON_OBJECT = "{}";
    public final static String APOLLO_DEFAULT_VALUE_JSON_ARRAY = "[]";
}