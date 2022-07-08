package org.mura.austin.cron.xxl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/25 8:50
 *
 * 执行器组信息
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class XxlJobGroup {
    //    主键
    private int id;

    //    执行器的appname
    private String appname;

    //    执行器组的标题
    private String title;

    /**
     * 执行器地址类型：
     * 0:自动注册
     * 1:手动录入
     */
    private int addressType;

    /**
     * 执行器地址列表，多地址逗号分隔(手动录入)
     */
    private String addressList;

    private Date updateTime;

    /**
     * registry list 执行器地址列表(系统注册)
     */
    private List<String> registryList;

    public List<String> getRegistryList() {
        if (addressList != null && addressList.trim().length() > 0) {
            registryList = new ArrayList<>(Arrays.asList(addressList.split(",")));
        }

        return registryList;
    }
}