参考项目：[java3y/austin](https://gitee.com/zhongfucheng/austin)

- 申请个人公众号（发送短信需要）
  > https://zhuanlan.zhihu.com/p/272826188

- 先注册腾讯短信服务
  > https://blog.csdn.net/Kevinnsm/article/details/119768203

- 访问localhost:8080/sendSms?phone=......即可发送短信，群发多个号码使用‘,’分割

目前使用的技术：
- spring boot
- lombok
- guava
- hutool
- okHttp（由于使用了腾讯sdk，所以暂时在代码中没有使用http调用，如果以后接入了其他厂商的服务，可能会使用http进行调用而不用各个厂商提供的sdk）
- logback
- mysql
- mybatis(-plus)
- tencentcloud-sdk-java（腾讯云短信sdk）
- kafka

目前能做的：
- 操作kafka
- 操作MySQL
- 单发、群发短信