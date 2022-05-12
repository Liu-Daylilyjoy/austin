package org.mura.austin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/12 19:56
 *
 * 腾讯短信参数
 *
 * 参数示例：
 * [{"sms_10":{"url":"sms.tencentcloudapi.com","region":"ap-guangzhou","secretId":"xxxxxxx","secretKey":"xxxxxxx","smsSdkAppId":"xxxxx","templateId":"xxxxx","signName":"xxxxx","supplierId":10,"supplierName":"腾讯云"}}]
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TencentSmsParam {
    /**
     * api相关
     */
    private String url;
    private String region ;

    /**
     * 账号相关
     */
    private String secretId;
    private String secretKey;
    private String smsSdkAppId;
    private String templateId;
    private String signName;

    /**
     * 标识渠道商Id
     */
    private Integer supplierId;

    /**
     * 标识渠道商名字
     */
    private String supplierName;
}
