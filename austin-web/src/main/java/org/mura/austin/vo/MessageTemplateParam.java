package org.mura.austin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/13 21:19
 *
 * 消息模板参数，目前用来管理分页
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateParam {

    /**
     * 当前页码
     */
    private Integer page ;

    /**
     * 当前页大小
     */
    private Integer perPage;


}