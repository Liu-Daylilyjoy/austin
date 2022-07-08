package org.mura.austin.constants;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 8:30
 *
 * 有关任务管理的URL常量以及一些关于XxlJob的常量
 */
public class XxlJobConstant {
    /**
     * xxl-job调度中心接口接口路径
     */
    public static final String LOGIN_URL = "/login";
    public static final String INSERT_URL = "/jobinfo/add";
    public static final String UPDATE_URL = "/jobinfo/update";
    public static final String DELETE_URL = "/jobinfo/remove";
    public static final String RUN_URL = "/jobinfo/start";
    public static final String STOP_URL = "/jobinfo/stop";

    /**
     * 执行器组接口路径
     */
    public static final String JOB_GROUP_PAGE_LIST = "/jobgroup/pageList";
    public static final String JOB_GROUP_INSERT_URL = "/jobgroup/save";


    /**
     * 执行任务名称
     */
    public static final String JOB_HANDLER_NAME = "austinJob";

    /**
     * 超时时间
     */
    public static final Integer TIME_OUT = 120;

    /**
     * 失败重试次数
     */
    public static final Integer RETRY_COUNT = 0;

    /**
     * 立即执行的任务 延迟时间（秒数），即立即执行的任务延时5秒后执行
     */
    public static final Integer DELAY_TIME = 10;
}
