package org.mura.austin.handler.config;

import org.mura.austin.handler.receiver.Receiver;
import org.mura.austin.handler.pending.Task;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 8:10
 *
 * handler模块的配置信息
 */
@Configuration
public class PrototypeBeanConfig {
    /**
     * 多例接收者
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Receiver receiver() {
        return new Receiver();
    }

    /**
     * 多例任务
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Task task() {
        return new Task();
    }
}
