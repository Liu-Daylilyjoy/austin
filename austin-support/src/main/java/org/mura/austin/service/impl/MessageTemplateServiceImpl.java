package org.mura.austin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mura.austin.domain.MessageTemplate;
import org.mura.austin.mapper.MessageTemplateMapper;
import org.mura.austin.service.MessageTemplateService;
import org.springframework.stereotype.Service;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 16:17
 */
@Service
public class MessageTemplateServiceImpl
        extends ServiceImpl<MessageTemplateMapper, MessageTemplate>
        implements MessageTemplateService {
}
