package org.mura.austin;

import org.mura.austin.pojo.SmsParam;
import org.mura.austin.script.TencentSmsScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;

/**
 * @author Akutagawa Murasame
 */

@SpringBootApplication
@RestController
public class AustinApplication {
    private TencentSmsScript tencentSmsScript;

    @Autowired
    public void setTencentSmsScript(TencentSmsScript tencentSmsScript) {
        this.tencentSmsScript = tencentSmsScript;
    }

    public static void main(String[] args) {
        SpringApplication.run(AustinApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam("phoneNumber") String phoneNumber) {
//        一开始我有点蒙，后来想到了Lombok的@Builder注解
        SmsParam smsParam = SmsParam.builder()
                .phones(new HashSet<>(Collections.singletonList(phoneNumber)))
                .content("123456")
                .build();

        return tencentSmsScript.send(smsParam);
    }
}
