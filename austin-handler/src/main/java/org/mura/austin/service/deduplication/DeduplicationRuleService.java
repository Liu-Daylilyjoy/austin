package org.mura.austin.service.deduplication;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.google.common.collect.Lists;
import org.mura.austin.constant.AustinConstant;
import org.mura.austin.domain.DeduplicationParam;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.service.deduplication.build.BuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/3 11:35
 *
 * 去重规则，如果有新的规则在这里组装即可
 */
@Service
public class DeduplicationRuleService {
    private static final String SERVICE = "Service";

    @ApolloConfig("boss.austin")
    private Config config;

    private ApplicationContext applicationContext;

    private BuilderFactory builderFactory;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setBuilderFactory(BuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    //去重服务责任链
    private static final List<String> DEDUPLICATION_LIST = Lists.newArrayList(DeduplicationConstants.CONTENT_DEDUPLICATION, DeduplicationConstants.FREQUENCY_DEDUPLICATION);

    public void deduplicate(TaskInfo taskInfo) {
//         配置示例:{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
//        尝试获取去重配置，无法获取则返回AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT("{}")
        String deduplication = config.getProperty(DeduplicationConstants.DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);

        DEDUPLICATION_LIST.forEach(
                key -> {
                    DeduplicationParam deduplicationParam = builderFactory.select(key).build(deduplication, key);
                    if (deduplicationParam != null) {
                        deduplicationParam.setTaskInfo(taskInfo);
                        DeduplicationService deduplicationService = findService(key + SERVICE);
                        deduplicationService.deduplication(deduplicationParam);
                    }
                }
        );
    }

//    一切都是为了拓展性
    private DeduplicationService findService(String beanName) {
        return applicationContext.getBean(beanName, DeduplicationService.class);
    }
}