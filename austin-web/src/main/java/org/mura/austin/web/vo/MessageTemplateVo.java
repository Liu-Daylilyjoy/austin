package org.mura.austin.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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
     * 消息模板List
     */
    private List<Map<String, Object>> rows;

    /**
     * 总条数
     */
    private Long count;
}
