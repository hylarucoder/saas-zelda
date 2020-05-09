# zelda

Kotlin/Spring Boot/Kubernetes 的微服务项目

## NOTE 

已废弃, 理由如下

- Spring Cloud 技术栈与 Java 绑定过于严重
- Java 编写代码过于冗长, 即便是使用 Kotlin, 依旧无法改变
- 教学版本基于线程模型, 并发量估计一般
- 生态相对而言没有想象中的丰富, 比如Kotlin对Java仅仅是增加了不少语法糖, 如果把标准库做的更好的话, 我就觉得不错了. 
- 没有 DB Migration 需要手动操作
- 基于注解的 ORM (JPA) 用起来实在体验较差
- 心智负担较为严重, 比如, 在编写Kotlin的时候需要照顾原有JavaSE的标准库JavaEE的一些标准以及Java的第三方库
- 使用Gradle+Kotlin DSL的编译过程太慢, 也没有良好的 Hot Reload 支持
- 缺乏社区的最佳实践
