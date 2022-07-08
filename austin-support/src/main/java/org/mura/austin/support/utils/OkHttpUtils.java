package org.mura.austin.support.utils;

import cn.hutool.core.map.MapUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/23 21:34
 */
@Slf4j
@Component
public class OkHttpUtils {
//    MediaType也就是http头中的Content-type
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    private OkHttpClient okHttpClient;

    @Autowired
    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }


    /**
     * get 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGetWithHeaders(String url, Map<String,String> headers) {
        return doGet(url, null, headers);
    }


    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGet(String url, Map<String, String> params, Map<String,String> headers) {
//        这里原本是先获取keySet，再由keySet获取value，不如直接获取EntrySet
        StringBuilder stringBuilder = new StringBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            boolean firstFlag = true;

            for (Map.Entry<String, String> param : entrySet) {
//                虽然这里只有一次是if成立的，但是我们也不用优化，可以搜搜CPU分支预测
                if (firstFlag) {
                    stringBuilder.append("?").append(param.getKey()).append("=").append(param.getValue());
                    firstFlag = false;
                } else {
                    stringBuilder.append("&").append(param.getKey()).append("=").append(param.getValue());
                }
            }
        }

//        上一步是将参数解析，还剩下url和请求头
        Request.Builder builder = getBuilderWithHeaders(headers);

//        还剩下url，在这一步添加
        Request request = builder.url(stringBuilder.toString()).build();

        log.info("do get request and url[{}]", stringBuilder);

//        执行request
        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doPost(String url, Map<String, String> params, Map<String,String> headers) {
//        这里OkHttp模拟了页面的表单做post提交
        FormBody.Builder formBuilder = new FormBody.Builder();

//        这里原本是先获取keySet，再由keySet获取value，不如直接获取EntrySet
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();

            for (Map.Entry<String, String> param : entrySet) {
                formBuilder.add(param.getKey(), param.getValue());
            }
        }

        Request.Builder builder = getBuilderWithHeaders(headers);

//        默认是get，这里需要调用post设置成post请求
        Request request = builder.url(url).post(formBuilder.build()).build();

        log.info("do post request and url[{}]", url);

        return execute(request);
    }


    /**
     * 获取request Builder
     *
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     */
    private Request.Builder getBuilderWithHeaders(Map<String,String> headers) {
        Request.Builder builder = new Request.Builder();
        if (!MapUtil.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return builder;
    }


    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据, json 字符串
     * @return string
     */
    public String doPostJson(String url, String json) {
        log.info("do post request and url[{}]", url);
        return executePost(url, json, JSON, null);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url     请求url地址
     * @param json    请求数据, json 字符串
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doPostJsonWithHeaders(String url, String json, Map<String, String> headers) {
        log.info("do post request and url[{}]", url);
        return executePost(url, json, JSON, headers);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        log.info("do post request and url[{}]", url);
        return executePost(url, xml, XML, null);
    }

    /**
     * 要注意短信是我们主动推送给客户端，所以我们发送的是请求报文
     */
    private String executePost(String url, String data, MediaType contentType, Map<String,String> headers) {
        RequestBody requestBody = RequestBody.create(data.getBytes(StandardCharsets.UTF_8), contentType);
        Request.Builder builder = getBuilderWithHeaders(headers);
        Request request = builder.url(url).post(requestBody).build();

        return execute(request);
    }

    private String execute(Request request) {
//        这里原本是初始化了一个null的Response，其实可以在try中释放资源
//        这里有点像js的promise，没学过就算了
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body =  response.body();
//                这里原本没有对body是否为null进行判断
                if (null != body) {
                    return body.string();
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            log.error(Throwables.getStackTraceAsString(e));
        }
        return "";
    }
}
