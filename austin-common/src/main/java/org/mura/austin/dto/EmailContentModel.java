package org.mura.austin.dto;

import lombok.*;

/**
 * @author Akutagawa Murasame
 *
 * 邮件消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailContentModel extends ContentModel {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容（可写入HTML）
     */
    private String content;
}
