问题：
1. session会话过期时间设置：ShiroConfig中的sessionManager设置，注意是单位毫秒！！！
2. redis解析java.time.LocalDateTime失败
   
   <a>https://www.cnblogs.com/chuzihang/p/8492863.html</a>
   
   > LocalDateTime属性加上注解 即可。
   > @JsonDeserialize(using = LocalDateTimeDeserializer.class) 
   > @JsonSerialize(using = LocalDateTimeSerializer.class)
   >
   > private LocalDateTime time;
3. 【参考】springboot+redis项目实战完整篇 https://www.jianshu.com/p/5596c3a4978d
