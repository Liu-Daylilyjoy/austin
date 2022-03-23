package org.mura.austin.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/23 21:04
 */
@Data
@Builder
public class SmsParam {
    /**
     * 需要发送的手机号（群发）
     */
    private Set<String> phones;

    /**
     * 发送文案
     */
    private String content;
}
