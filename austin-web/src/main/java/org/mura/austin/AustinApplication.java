package org.mura.austin;

import org.mura.austin.pojo.SmsParam;
import org.mura.austin.script.TencentSmsScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String hello() {
//        一开始我有点蒙，后来想到了Lombok的@Builder注解
        SmsParam smsParam = SmsParam.builder()
//                这是我的另一个手机号，但是我从来不把装了这个卡的手机带在身上
                .phones(new HashSet<>(Collections.singletonList("17386150536")))
                .content("123456")
                .build();

        return tencentSmsScript.send(smsParam);
    }
}
