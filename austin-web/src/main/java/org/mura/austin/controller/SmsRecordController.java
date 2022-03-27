package org.mura.austin.controller;

import org.mura.austin.dao.SmsRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 14:38
 *
 * 设置短信记录接口
 *
 * 还没做完，暂时不加入到容器
 */
//@RestController
public class SmsRecordController {
    private SmsRecordDao smsRecordDao;

    @Autowired
    public void setSmsRecordDao(SmsRecordDao smsRecordDao) {
        this.smsRecordDao = smsRecordDao;
    }

    /**
     * test insert
     */
    @GetMapping("/insert")
    public String insert(Integer phone) {
        return null;
    }

    /**
     * test query
     */
    @GetMapping("/query")
    public String query() {

        return null;
    }
}