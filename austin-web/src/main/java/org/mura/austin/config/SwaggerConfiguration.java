package org.mura.austin.config;

import io.swagger.annotations.ApiModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/15 15:13
 *
 * 接口文档配置
 */
@Configuration
@EnableOpenApi  // 开启Swagger3
@ApiModel
public class SwaggerConfiguration {
    /**
     * C端用户的接口文档
     */
    @Bean
    public Docket webApiDoc() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("用户端接口文档")
                .pathMapping("/")
                //定义是否开启Swagger，false是关闭，可以通过变量去控制，线上关闭
                .enable(true)
                //配置文档的元信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.mura.austin.controller"))
                //正则匹配请求路径，并分配到当前项目组
                //.paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("austin平台")
                .description("消息推送接口接口文档")
                .contact(new Contact("芥川村雨", "https://github.com/Akutagawa-Murasame/austin", "akutagawa_murasame@foxmail.com"))
                .version("v1.0")
                .build();
    }
}
