package org.mura.austin.service.api.impl.config;

import org.mura.austin.service.api.impl.action.AssembleAction;
import org.mura.austin.service.api.impl.action.PostParamCheckAction;
import org.mura.austin.service.api.impl.action.PreParamCheckAction;
import org.mura.austin.service.api.impl.action.SendMqAction;
import org.mura.austin.service.api.enums.BusinessCode;
import org.mura.austin.support.pipeline.BusinessProcess;
import org.mura.austin.support.pipeline.ProcessController;
import org.mura.austin.support.pipeline.ProcessTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Akutagawa Murasame
 */
@Configuration
public class PipelineConfig {
    /**
     * 普通发送执行流程
     * 1、前置参数校验
     * 2、组装参数
     * 3、后置参数校验
     * 4、发送消息至MQ
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        ArrayList<BusinessProcess> businessProcesses = new ArrayList<>();

//        组装流程，形成责任链
        businessProcesses.add(preParamCheckAction());
        businessProcesses.add(assembleAction());
        businessProcesses.add(postParamCheckAction());
        businessProcesses.add(sendMqAction());

        processTemplate.setProcessList(businessProcesses);

        return processTemplate;
    }

    /**
     * pipeline流程控制器
     * 目前暂定只有 普通发送的流程
     * 后续扩展则加BusinessCode和ProcessTemplate
     */
    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }

    /**
     * 前置参数校验Action
     */
    @Bean
    public PreParamCheckAction preParamCheckAction() {
        return new PreParamCheckAction();
    }

    /**
     * 组装参数Action
     */
    @Bean
    public AssembleAction assembleAction() {
        return new AssembleAction();
    }

    /**
     * 后置参数校验Action
     */
    @Bean
    public PostParamCheckAction postParamCheckAction() {
        return new PostParamCheckAction();
    }

    /**
     * 参数校验Action
     */
    @Bean
    public PreParamCheckAction preParamAction() {
        return new PreParamCheckAction();
    }

    /**
     * 发送消息至MQ的Action
     */
    @Bean
    public SendMqAction sendMqAction() {
        return new SendMqAction();
    }
}