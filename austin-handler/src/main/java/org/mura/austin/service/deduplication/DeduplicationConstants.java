package org.mura.austin.service.deduplication;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:15
 */
public class DeduplicationConstants {
    /**
     * 配置样例：
     * {
     *      "contentDeduplication":{
     *          "num":1,
     *          "time":300
     *      },
     *      "frequencyDeduplication":{
     *          "num":5
     *      }
     * }
     */
    public static final String DEDUPLICATION_RULE_KEY = "deduplication";
    public static final String CONTENT_DEDUPLICATION = "contentDeduplication";
    public static final String FREQUENCY_DEDUPLICATION = "frequencyDeduplication";
}
