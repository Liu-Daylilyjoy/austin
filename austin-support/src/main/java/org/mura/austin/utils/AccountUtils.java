package org.mura.austin.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.mura.austin.constant.AustinConstant;
import org.springframework.stereotype.Component;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/12 20:29
 *
 * 获取账号信息工具类
 */
@Component
public class AccountUtils {
    @ApolloConfig("boss.austin")
    private Config config;

    public <T> T getAccount(Integer sendAccount, String apolloKey, String prefix, T t) {
        String accountValues = config.getProperty(apolloKey, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(accountValues);

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Object object = jsonObject.getObject(prefix + sendAccount, t.getClass());

            if (object != null) {
                return (T) object;
            }
        }

        return null;
    }
}