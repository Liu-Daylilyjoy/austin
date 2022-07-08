package org.mura.austin.cron.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Akutagawa Murasame
 * @date 2022/7/8 14:09
 *
 * 这个类记录了csv文件的每一行的信息（接收者+消息参数）
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrowdInfoVo implements Serializable {
    /**
     * 接收者id
     */
    private String id;

    /**
     * 参数信息
     */
    private Map<String, String> params;
}
