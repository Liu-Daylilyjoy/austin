package org.mura.austin.pipeline;

import lombok.Builder;

/**
 * @author Akutagawa Murasame
 *
 * 流程处理的结果
 */
@Builder
public class ProcessResponse {
    /**
     * 返回值编码
     */
    private final String code;

    /**
     * 返回值描述
     */
    private final String description;
}
