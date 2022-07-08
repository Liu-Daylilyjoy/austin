package org.mura.austin.cron.xxl.enums;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 8:49
 *
 * 胶水语言的类型枚举（要使用哪种胶水语言）
 */
public enum GlueTypeEnum {
    BEAN,
    GLUE_GROOVY,
    GLUE_SHELL,
    GLUE_PYTHON,
    GLUE_PHP,
    GLUE_NODEJS,
    GLUE_POWERSHELL;

    GlueTypeEnum() {}
}
