package org.mura.austin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import org.mura.austin.constants.XxlJobConstant;
import org.mura.austin.entity.XxlJobGroup;
import org.mura.austin.entity.XxlJobInfo;
import org.mura.austin.enums.ResponseStatusEnum;
import org.mura.austin.service.CronTaskService;
import org.mura.austin.vo.BasicResultVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 9:25
 *
 * 设置定时任务的类
 */
@Service
@Slf4j
public class CronTaskServiceImpl implements CronTaskService {
    @Value("${xxl.job.admin.username}")
    private String xxlUserName;

    @Value("${xxl.job.admin.password}")
    private String xxlPassword;

    @Value("${xxl.job.admin.addresses}")
    private String xxlAddresses;

    @Override
    public BasicResultVo saveCronTask(XxlJobInfo xxlJobInfo) {
//        将job信息转化成map,方便做成表单
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobInfo), Map.class);

//        根据信息中是否有id来判断是添加还是更新
        String path = xxlJobInfo.getId() == null ? xxlAddresses + XxlJobConstant.INSERT_URL
                : xxlAddresses + XxlJobConstant.UPDATE_URL;

        HttpResponse response = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();

            // 插入时需要返回Id，而更新时不需要
            if (path.contains(XxlJobConstant.INSERT_URL) && response.isOk()) {
//                从xxl-job的源码中可以看出,content字段返回的是插入字段后数据库中的taskId
                Integer taskId = Integer.parseInt(String.valueOf(JSON.parseObject(response.body()).get("content")));

                return BasicResultVo.success(taskId);
            } else if (response.isOk()) {
                return BasicResultVo.success();
            }
        } catch (Exception e) {
            log.error("CronTaskService#saveTask fail:{}", JSON.toJSONString(response.body()));

            return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }

        return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
    }

    @Override
    public BasicResultVo deleteCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);

        String path = xxlAddresses + XxlJobConstant.DELETE_URL;

        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();

        if (!response.isOk()) {
            log.error("CronTaskService#deleteCronTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }

        return BasicResultVo.success();
    }

    @Override
    public BasicResultVo startCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);

        String path = xxlAddresses + XxlJobConstant.RUN_URL;

        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();

        if (!response.isOk()) {
            log.error("CronTaskService#startCronTask fail:{}", JSON.toJSONString(response.body()));

            return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }

        return BasicResultVo.success();
    }

    @Override
    public BasicResultVo stopCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);

        String path = xxlAddresses + XxlJobConstant.STOP_URL;

        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();

        if (!response.isOk()) {
            log.error("CronTaskService#stopCronTask fail:{}", JSON.toJSONString(response.body()));

            return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }

        return BasicResultVo.success();
    }

    @Override
    public BasicResultVo getGroupId(String appName, String title) {
//        获取所有的jobgroup
        String path = xxlAddresses + XxlJobConstant.JOB_GROUP_PAGE_LIST;

        HashMap<String, Object> params = new HashMap<>();
        params.put("appname", appName);
        params.put("title", title);

        HttpResponse response = null;
        try {
//            不传appname和title时,返回的是所有的jobgroup
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();

            Integer id = JSON.parseObject(response.body()).getJSONArray("data").getJSONObject(0).getInteger("id");

            if (response.isOk() && id != null) {
                return BasicResultVo.success(id);
            }
        } catch (Exception e) {
            log.error("CronTaskService#getGroupId fail:{}", JSON.toJSONString(response.body()));

            return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }

        return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
    }

    @Override
    public BasicResultVo createGroup(XxlJobGroup xxlJobGroup) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobGroup), Map.class);

        String path = xxlAddresses + XxlJobConstant.JOB_GROUP_INSERT_URL;

        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();

        if (!response.isOk()) {
            log.error("CronTaskService#createGroup fail:{}", JSON.toJSONString(response.body()));

            return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }

        return BasicResultVo.success();
    }

    /**
     * 获取xxl cookie，由于定时任务调度管理需要登录，因此将登录信息存在cookie中
     */
    private String getCookie() {
        Map<String, Object> hashMap = new HashMap<>(8);
        hashMap.put("userName", xxlUserName);
        hashMap.put("password", xxlPassword);
        hashMap.put("randomCode", IdUtil.fastSimpleUUID());

        String path = xxlAddresses + XxlJobConstant.LOGIN_URL;

        HttpResponse response = HttpRequest.post(path).form(hashMap).execute();

        if (response.isOk()) {
            List<HttpCookie> cookies = response.getCookies();

            StringBuilder sb = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                sb.append(cookie.toString());
            }

            return sb.toString();
        }

        log.error("CronTaskService#getCookie fail:{}", JSON.parseObject(response.body()));

        return null;
    }
}