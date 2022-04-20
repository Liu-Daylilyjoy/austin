package org.mura.austin.config;

import org.mura.austin.action.AssembleAction;
import org.mura.austin.action.PreParamAction;
import org.mura.austin.action.SendMqAction;
import org.mura.austin.enums.BusinessCode;
import org.mura.austin.pipeline.BusinessProcess;
import org.mura.austin.pipeline.ProcessController;
import org.mura.austin.pipeline.ProcessTemplate;
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
     * 1、参数校验
     * 2、组装参数
     * 3、发送消息至MQ
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        ArrayList<BusinessProcess> businessProcesses = new ArrayList<>();

//        组装流程，形成责任链
        businessProcesses.add(preParamAction());
        businessProcesses.add(assembleAction());
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
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(16);
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }


    /**
     * 组装参数Action
     */
    @Bean
    public AssembleAction assembleAction() {
        return new AssembleAction();
    }

    /**
     * 参数校验Action
     */
    @Bean
    public PreParamAction preParamAction() {
        return new PreParamAction();
    }

    /**
     * 发送消息至MQ的Action
     */
    @Bean
    public SendMqAction sendMqAction() {
        return new SendMqAction();
    }
}