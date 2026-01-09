# 目标
质疑若依➡️ 学习若依 ➡️成为若依

致敬若依：[若依](https://gitee.com/y_project/RuoYi-Vue)

# 进度

- 20260109 目前只复现了登录这个功能...前路漫漫

# 优势

1. 修改了各模块的继承逻辑，本项目删除了若依中的admin模块（一开始删的，但我现在越来越觉得这个模块确实有存在的必要...），各模块之前的继承关系是：system➡️framework➡️common。

2. UserAgent的解析工具不使用[bitwalker](https://mvnrepository.com/artifact/nl.bitwalker/UserAgentUtils)的，原因是有强迫症，不想在pom.xml里看到有【在依赖项中发现的漏洞】的警告。但我也没有复现这个东西，直接用了Hutool中的[UA工具类](https://doc.hutool.cn/pages/UserAgentUtil/#%E7%94%B1%E6%9D%A5)。没有引入，只做搬运，致敬[Hutool](https://doc.hutool.cn/)。

   - 存在问题：目前这个工具没法识别win11的设备，原因就是win10和win11的UserAgent是一样的。而且UserAgent本身无法识别目标是win还是win server。

   - 一些资料：
     - [官方：使用 User-Agent 客户端提示检测Windows 11和 CPU 体系结构](https://learn.microsoft.com/zh-cn/microsoft-edge/web-platform/how-to-detect-win11)
     - [稀土掘金：Java判断请求来自win10还是win11竟然这么复杂](https://juejin.cn/post/7398511534744338447)
     - [Hutool项目中有相关的issue但并没有被解决](https://github.com/chinabugotech/hutool/issues/3745)




3. 拥抱新特性，SpringBoot目前用的版本是3.5.8，还没法直接到4.0.0，主要是想用的[Druid连接池](https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter)都还不支持4.0版本。Java版本是25。javax也都修改成jakarta了。

4. 序列化工具使用jackson。这样Redis里看的内容更清爽直观了（也是强迫症）。

5. 时间类型的字段都使用LocalDateTime这种类，为此还额外引入了[jackson-datatype-jsr310](https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310)这个依赖，并对jackson做了全局的序列化参数配置，从而解决Java 8 date/time type `java.time.LocalDateTime` not supported by default的报错。
6. 统一了AjaxResult的输出格式。返回结果应该服从一定的格式规范，这里用的是这种：

```json
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        data
    }
}
```



