package org.mura.austin.cron.xxl.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;

import com.google.common.base.Throwables;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.cron.xxl.constants.XxlJobConstant;
import org.mura.austin.cron.xxl.entity.XxlJobGroup;
import org.mura.austin.cron.xxl.entity.XxlJobInfo;
import org.mura.austin.common.enums.ResponseStatusEnum;
import org.mura.austin.cron.xxl.service.CronTaskService;
import org.mura.austin.common.vo.BasicResultVo;
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
@Slf4j
@Service
public class CronTaskServiceImpl implements CronTaskService {
    @Value("${xxl.job.admin.username}")
    private String xxlUserName;

    @Value("${xxl.job.admin.password}")
    private String xxlPassword;

    @Value("${xxl.job.admin.addresses}")
    private String xxlAddresses;
    
    @Override
    public BasicResultVo saveCronTask(XxlJobInfo xxlJobInfo) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobInfo), Map.class);
        String path = xxlJobInfo.getId() == null ? xxlAddresses + XxlJobConstant.INSERT_URL
                : xxlAddresses + XxlJobConstant.UPDATE_URL;

        HttpResponse response;
        ReturnT returnT = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);

            // 插入时需要返回Id，而更新时不需要
            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
//                从xxl-job的源码中可以看出,content字段返回的是插入字段后数据库中的taskId
                if (path.contains(XxlJobConstant.INSERT_URL)) {
                    Integer taskId = Integer.parseInt(String.valueOf(returnT.getContent()));

                    return BasicResultVo.success(taskId);
                } else if (path.contains(XxlJobConstant.UPDATE_URL)) {
                    return BasicResultVo.success();
                }
            }
        } catch (Exception e) {
            log.error("CronTaskService#saveTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(xxlJobInfo), JSON.toJSONString(returnT));
        }

        return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResultVo deleteCronTask(Integer taskId) {
        String path = xxlAddresses + XxlJobConstant.DELETE_URL;

        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("id", taskId);

        HttpResponse response;
        ReturnT returnT = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);

            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResultVo.success();
            }
        } catch (Exception e) {
            log.error("CronTaskService#deleteCronTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(returnT));
        }

        return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResultVo startCronTask(Integer taskId) {
        String path = xxlAddresses + XxlJobConstant.RUN_URL;

        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("id", taskId);

        HttpResponse response;
        ReturnT returnT = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);

            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResultVo.success();
            }
        } catch (Exception e) {
            log.error("CronTaskService#startCronTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(returnT));
        }

        return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResultVo stopCronTask(Integer taskId) {
        String path = xxlAddresses + XxlJobConstant.STOP_URL;

        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("id", taskId);

        HttpResponse response;
        ReturnT returnT = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);

            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResultVo.success();
            }
        } catch (Exception e) {
            log.error("CronTaskService#stopCronTask fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(returnT));
        }

        return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResultVo getGroupId(String appName, String title) {
        String path = xxlAddresses + XxlJobConstant.JOB_GROUP_PAGE_LIST;

        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("appname", appName);
        params.put("title", title);

        HttpResponse response = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            Integer id = JSON.parseObject(response.body()).getJSONArray("data").getJSONObject(0).getInteger("id");

            if (response.isOk() && id != null) {
                return BasicResultVo.success(id);
            }
        } catch (Exception e) {
            log.error("CronTaskService#getGroupId fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(response.body()));
        }

        return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
    }

    @Override
    public BasicResultVo createGroup(XxlJobGroup xxlJobGroup) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobGroup), Map.class);
        String path = xxlAddresses + XxlJobConstant.JOB_GROUP_INSERT_URL;

        HttpResponse response;
        ReturnT returnT = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);

            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResultVo.success();
            }
        } catch (Exception e) {
            log.error("CronTaskService#createGroup fail,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(returnT));
        }

        return BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR, JSON.toJSONString(returnT));
    }

    /**
     * 获取xxl cookie，由于定时任务调度管理需要登录，因此将登录信息存在cookie中
     */
    private String getCookie() {
        Map<String, Object> params = MapUtil.newHashMap();
        params.put("userName", xxlUserName);
        params.put("password", xxlPassword);
        params.put("randomCode", IdUtil.fastSimpleUUID());

        String path = xxlAddresses + XxlJobConstant.LOGIN_URL;
        HttpResponse response = null;
        try {
            response = HttpRequest.post(path).form(params).execute();

            if (response.isOk()) {
                List<HttpCookie> cookies = response.getCookies();
                StringBuilder sb = new StringBuilder();

                for (HttpCookie cookie : cookies) {
                    sb.append(cookie.toString());
                }

                return sb.toString();
            }
        } catch (Exception e) {
            log.error("CronTaskService#createGroup getCookie,e:{},param:{},response:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(params), JSON.toJSONString(response));
        }

        return null;
    }
}