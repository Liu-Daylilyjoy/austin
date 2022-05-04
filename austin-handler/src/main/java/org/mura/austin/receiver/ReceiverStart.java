package org.mura.austin.receiver;

import org.mura.austin.utils.GroupIdMappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 10:28
 *
 * 启动消费者
 */
@Service
public class ReceiverStart {
    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * receiver的消费方法常量
     */
    private static final String RECEIVER_METHOD_NAME = "Receiver.consume";

    /**
     * 获取得到所有的groupId
     */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 下标（用于迭代groupIds位置）
     */
    private static Integer index = 0;

    /**
     * 为每个渠道不同的消息类型创建一个Receiver对象
     *
     * 调用applicationContext的getBean方法，使spring创建对象到容器中
     */
    @PostConstruct
    public void init() {
        for (int i = 0; i < groupIds.size(); ++i) {
            applicationContext.getBean(Receiver.class);
        }
    }

    /**
     * 给每个Receiver对象的consumer方法 @KafkaListener赋值相应的groupId
     *
     * 	AnnotationEnhancer bean definitions must be declared static
     * 	because they are required very early in the application context’s lifecycle.
     *
     * 首先构造ReceiverStart类，之后执行@PostConstruct注解的方法init，其中每次循环
     * 调用一次getBean方法，当bean已注册但未实例化时spring会进行实例化，实例化后会执行BeanPostProcessor
     * 之后bean初始化成功，接着进行下一次循环，最终创建M x N个Receiver（M、N分别为ChannelType和MessageType的value个数）
     */
    @Bean
    public static KafkaListenerAnnotationBeanPostProcessor.AnnotationEnhancer groupIdEnhancer() {
        return (attrs, element) -> {
            if (element instanceof Method) {
                String name = ((Method) element).getDeclaringClass().getSimpleName() + "." + ((Method) element).getName();

//                如果这个注解的方法是我们的目标方法Receiver.consume
                if (RECEIVER_METHOD_NAME.equals(name)) {
//                    将KafkaListener注解的groupId设置为GroupIdEnhancer中预设的groupId，避免设置过多的Receiver
                    attrs.put("groupId", groupIds.get(index));

                    ++index;
                }
            }

            return attrs;
        };
    }
}
