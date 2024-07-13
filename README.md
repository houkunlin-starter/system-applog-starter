[![](https://jitpack.io/v/houkunlin/system-applog-starter.svg)](https://jitpack.io/#houkunlin/system-applog-starter)
[![Maven Central](https://img.shields.io/maven-central/v/com.houkunlin/system-applog-starter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.houkunlin%22%20AND%20a:%22system-applog-starter%22)

# 系统日志 Starter

> v1.1.0 起基于 SpringBoot 3.0.0 编译发布，JDK 最低要求 JDK17
>
> v1.2.0 起基于 SpringBoot 3.3.0 编译发布，JDK 最低要求 JDK17
>
> v1.2.0 产生破坏性变更，移除 `AppLogEvent` 事件对象，增加 `AppLogHandler`
> 处理器对象，包名由 `com.houkunlin.system.applog.starter` 改为 `com.houkunlin.system.applog`

**Maven**

```xml
<dependency>
    <groupId>com.houkunlin</groupId>
    <artifactId>system-applog-starter</artifactId>
    <version>${latest.version}</version>
</dependency>
```

**Gradle**

```groovy
implementation "com.houkunlin:system-applog-starter:${latest.version}"
```


## 如何使用

### 在 `Controller` 层使用

在 Controller 接口中使用 `com.houkunlin.system.applog.AppLog` 注解，示例如下：

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

日志内容模板语法可自行定义，自行定义时请实现 [TemplateParser.java](src%2Fmain%2Fjava%2Fcom%2Fhoukunlin%2Fsystem%2Fapplog%2FTemplateParser.java)
接口，
默认提供的 [TemplateParserDefaultImpl.java](src%2Fmain%2Fjava%2Fcom%2Fhoukunlin%2Fsystem%2Fapplog%2FTemplateParserDefaultImpl.java)
支持 SpEL 表达式（模板）解析。


`AppLog` 参数列表：

| 字段       | 类型   | 说明                                                         | SpEL支持 |
| ---------- | ------ | ------------------------------------------------------------ | -------- |
| value      | String | 日志内容                                                     | 是       |
| errorValue | String | 发生错误时的日志内容，默认为与 value 内容一致，但在后面加了 exception.getMessage() 的内容 | 是       |
| type       | String | 操作类型（类型需要自行传入）                                 | 否       |
| createdBy  | String | 日志创建人                                                   | 是       |

### 在业务代码中的使用示例

示例代码：[src/test/java/com/houkunlin/system/applog/AppLoggerTest.java](src/test/java/com/houkunlin/system/applog/AppLoggerTest.java)

```java
public class Test {
    // 获取应用日志对象
    public static final AppLogger APP_LOGGER = AppLoggerFactory.getLogger("test");

    public void test() {
        // 像是使用 SLF4j 日志一样调用接口
        APP_LOGGER.log("Hello World"); // Hello World
        APP_LOGGER.log("Hello World {}", 12); // Hello World 12
        APP_LOGGER.log("Hello World {}", new RuntimeException("ERROR")); // Hello World ERROR
        APP_LOGGER.logBiz("bizId", "Hello World"); // bizId:bizId; Hello World
        APP_LOGGER.logBiz("bizId", "Hello World {}", 12); // bizId:bizId; Hello World 12
        APP_LOGGER.logBiz("bizId", "Hello World {}", new RuntimeException("ERROR")); // bizId:bizId; Hello World ERROR
    }
}
```

## 如何把日志存入到数据库中

日志会封装成 `com.houkunlin.system.applog.AppLogInfo` 对象传给其他系统

### 本地存储

实现 `com.houkunlin.system.applog.AppLogHandler` 接口并扫描到SpringBoot环境中，自行实现日志信息存储



### 与其他系统协同，把日志存入其他系统

依赖 Rabbitmq 环境，自行监听 Rabbitmq 消息队列数据，从 `com.houkunlin.system.applog.AppLogProperties.mqQueue` 队列中获取日志数据。
示例代码（仅供参考）

```java
@RabbitListener(queues = "#{appLogProperties.mqQueue}")
public void readLog(AppLogInfo info) {
    logger.info("通过 MQ 获取到日志信息：{}", info);
}
```
