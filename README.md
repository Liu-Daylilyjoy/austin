为了简历而写的项目，学习了Java3y的austin项目

[java3y/austin](https://gitee.com/zhongfucheng/austin)

- 申请个人公众号（发送短信需要）
  > https://zhuanlan.zhihu.com/p/272826188

- 先注册腾讯短信服务
  > https://blog.csdn.net/Kevinnsm/article/details/119768203

- 访问localhost:8080/send_message?phoneNumber=...&content=...
即可发送短信（content有可能不能超过6位，不同的用户不一样）

```注：当前版本没有用到support模块，目前只能单发，不能群发```

目前使用的技术：
- spring boot
- lombok
- guava
- hutool
- okHttp
- logback
- mybatis
- tencentcloud-sdk-java（腾讯云短信）

[环境配置（重要）](CONFIG.md)

# 里程碑：

第一次成功发短信
![img_1.png](img_1.png)