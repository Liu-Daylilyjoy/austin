package org.mura.austin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mura.austin.domain.MessageTemplate;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/19 15:16
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateVo {

    /**
     * 消息模板列表
     */
    private Iterable<MessageTemplate> rows;

    /**
     * 总条数
     */
    private Long count;
}
