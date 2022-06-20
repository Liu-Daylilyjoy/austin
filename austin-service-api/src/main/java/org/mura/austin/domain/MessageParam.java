package org.mura.austin.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author Akutagawa Murasame
 *
 * 消息参数
 */
@Data
@Accessors(chain = true)
@Builder
public class MessageParam {
    /**
     * 接收者
     * 多个用,逗号分隔开
     */
    private String receiver;

    /**
     * 消息内容中的可变部分(占位符替换)，可选
     */
    private Map<String, String> variables;

    /**
     * 扩展参数，可选
     */
    private Map<String,String> extra;
}
