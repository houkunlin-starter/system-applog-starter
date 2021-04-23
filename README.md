# 系统日志 Starter

## 如何使用

在 Controller 接口中使用 `com.houkunlin.system.applog.starter.AppLog` 注解，示例如下：

```java
@AppLog("有人获取了用户信息")
public Json getUser(){
  return new Json()
}
```

使用示例：

- `@AppLog("接口操作说明，方法名称：#{method.name}")`
- `@AppLog("接口操作说明，方法参数：#{args[0].toString()}")`
- `@AppLog("接口操作说明，方法参数：#{#id}")` 其中的 id 为方法参数名称
- `@AppLog("接口操作说明，当前对象：#{target.toString()}")`
- `@AppLog("接口操作说明，当前对象class：#{targetClass.getName()}")`
- `@AppLog("接口操作说明，返回结果：#{result?.toString()?:'无返回值'}")`



`AppLog` 参数列表：

| 字段       | 类型   | 说明                                                         | SpEL支持 |
| ---------- | ------ | ------------------------------------------------------------ | -------- |
| value      | String | 日志内容                                                     | 是       |
| errorValue | String | 发生错误时的日志内容，默认为与 value 内容一致，但在后面加了 exception.getMessage() 的内容 | 是       |
| type       | String | 操作类型（类型需要自行传入）                                 | 否       |
| createdBy  | String | 日志创建人                                                   | 是       |



## 如何把日志存入到数据库中

日志会封装成 `com.houkunlin.system.applog.starter.AppLogInfo` 对象传给其他系统

### 本地存储

实现 `com.houkunlin.system.applog.starter.store.AppLogStore` 接口并扫描到SpringBoot环境中



### 与其他系统协同，把日志存入其他系统

依赖 Rabbitmq 环境，自行监听 Rabbitmq 消息队列数据，从 `com.houkunlin.system.applog.starter.AppLogProperties.mqQueue` 队列中获取日志数据。
示例代码

```java
@RabbitListener(queues = "#{appLogProperties.mqQueue}")
public void readLog(AppLogInfo info) {
    logger.info("通过 MQ 获取到日志信息：{}", info);
}
```
