特别鸣谢：[java3y/austin](https://gitee.com/zhongfucheng/austin)

运行环境：
- jdk1.8+ 
- kafka(2.8)
- redis(6.0)
- mysql(8.0.28)
- Apollo [apollo使用docker快速部署](https://www.apolloconfig.com/#/zh/deployment/quick-start-docker) ```注意检查端口是否冲突```
- 系统监控告警是否存在对项目无影响，项目使用的监控是Prometheus，对于QPS等实时数据不太友好
- 业务监控暂时没有接入，后期考虑使用ELK（Elasticsearch + Logstash + Kibana）

- 申请个人公众号（发送短信需要）
  > https://zhuanlan.zhihu.com/p/272826188

- 先注册腾讯短信服务
  > https://blog.csdn.net/Kevinnsm/article/details/119768203
- 配置handler模块中的smsEg.properties文件
- 访问localhost:8080/sendSms?phone=......即可发送短信，群发多个号码使用','分割

目前使用的技术：
- docker(容器技术，快速部署中间件)
- Spring Boot(快速部署环境)
- Lombok(快速开发)
- Guava(安全工具类)
- hutool(便捷开发工具类)
- OkHttp(由于使用了腾讯sdk，所以暂时在代码中没有使用http调用，如果以后接入了其他厂商的服务，可能会使用http进行调用而不用各个厂商提供的sdk)
- Logback(日志)
- MySQL(数据库)
- MyBatis(-Plus)(数据库访问)
- tencentcloud-sdk-java(腾讯云短信sdk)
- Apache Kafka(消息队列，抗压削峰)
- Redis(NoSQL，设置唯一key，消息去重)
- Prometheus(监控核心组件)
- alertmanager(告警组件，目前未用到，属于Prometheus)
- Grafana(可视化，与监控告警配合使用)
- cadvisor(获取服务器上所有docker容器的状态)
- node-exporter(获取服务器设备情况，属于Prometheus)
- Apollo(分布式配置中心，统一配置，动态变化，降低系统维护难度)

目前能做的：
- logback记录运行时日志
- 单发、群发短信
- M分钟内重复消息去重、一天内同一渠道同一用户最多收到N条消息
- 丢弃使用了某一消息模板的消息
- 可以在项目运行时动态配置去重、功能开关等

业务解决方案：
- 多个短信接口供应商解耦与多种消息发送方式解耦：
  > ```austin-handler``` 使用***适配器模式***，定义通用接口，可以在不更改原有代码的情况下拓展业务渠道，为了信息发送不局限于短信，使用了***适配器模式***配合***组合模式***定义通用的消息处理接口进行业务拓展
- 不同渠道与种类消息实现消费分离：
  > ```austin-handler``` kafka多个group之间消费是分离的，每个group都会接受到来自同一个topic的相同消息，所以接收到消息后会进行判断，只消费属于自己的消息；在代码中使用了kafka的***AnnotationBeanPostProcessor***动态地指定了Receiver的groupId，并且使用spring的@Header注解作为消费方法的入参，以便Receiver进行对消息是否属于自己管辖的判断（使用groupId和消息中带有的groupId对比）
- 消息高性能消费：
  > ```austin-handler``` 定义一个***工厂模式***方法，使用map，消费渠道作为key，线程池作为value，实现不同的消费渠道使用不同的线程池，因为线程池需要占用一定的资源，所以对于不同的线程池应当设置不同的运行参数；使用线程池进行消息消费，可以实现单个渠道的消息消费也是多线程的，这就保证了消息消费的高性能
- 不合格的消息不进入消息队列：
  > ```austin-service-api-impl``` 如果不进行参数校验直接将所有的消息都存入消息队列，终究是一种浪费，因此在消息预处理阶段，使用***责任链模式***，经历参数前置校验、参数组合、参数后置校验后再放入消息队列
- 消息去重：
  > ```austin-handler``` 在发送消息前进行消息内容和发送渠道的比对，默认5分钟内同一用户收到的相同内容的消息不发送，默认一天内（24：00）每个用户最多只能收到某个渠道发送的5条消息，大于等于5条的消息进行去重，由于项目的定位是一个消息发送平台，因此不能与业务方耦合，所以使用***模板模式***在代码中***AbstractDeduplicationService***类中定义了一个抽象方法getDeduplicationKey用于业务方自行设计去重逻辑
- 消息丢弃：
  > ```austin-handler``` 本可以在austin-api处实现丢弃逻辑，但是可能大部分时候消息是放在在MQ中的，所以丢弃功能在austin-handler中实现