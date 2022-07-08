package org.mura.austin.common.dto;

import lombok.*;

/**
 * @author Akutagawa Murasame
 *
 * 短信消息，这个项目也是因此产生
 *
 * 在前端填写的时候分开，但最后处理的时候会将url拼接在content上
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsContentModel extends ContentModel {
    /**
     * 短信发送内容
     */
    private String content;

    /**
     * 短信发送链接
     */
    private String url;
}
