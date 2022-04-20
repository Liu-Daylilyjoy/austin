学习了Java3y的austin项目

[java3y/austin](https://gitee.com/zhongfucheng/austin)

- 申请个人公众号（发送短信需要）
  > https://zhuanlan.zhihu.com/p/272826188

- 先注册腾讯短信服务
  > https://blog.csdn.net/Kevinnsm/article/details/119768203

- 访问localhost:8080/send_message?phoneNumber=...&content=...
即可发送短信（content有可能不能超过6位，不同的用户不一样）

目前使用的技术：
- spring boot
- lombok
- guava
- hutool
- okHttp
- logback
- mybatis(-plus)
- tencentcloud-sdk-java（腾讯云短信）
- kafka
- mysql

目前能做的：
- 操作kafka
- 操作MySQL
- 发送短信