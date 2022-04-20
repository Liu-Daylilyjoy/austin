package org.mura.austin.pipeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.mura.austin.vo.BasicResultVo;

/**
 * @author Akutagawa Murasame
 *
 * 业务处理责任链上下文
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class ProcessContext {
    /**
     * 责任链标识
     */
    private String code;

    /**
     * 上下文存储模型
     */
    private ProcessModel processModel;

    /**
     * 责任链终端标志
     */
    private Boolean needBreak = false;

    /**
     * 流程处理的结果
     */
    BasicResultVo<?> response = BasicResultVo.success();
}