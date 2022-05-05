package org.mura.austin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Akutagawa Murasame
 */

@SpringBootApplication
public class AustinApplication {
    public static void main(String[] args) {
//        TODO Apollo的地址，端口是eureka管理界面的端口
        System.setProperty("apollo.config-service", "http://192.168.159.128:7000");
        SpringApplication.run(AustinApplication.class, args);
    }
}