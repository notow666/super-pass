# 内网映射

> 采用`java` 原生 `BIO`实现，扩展空间很大，有兴趣的小伙伴可以参与进来

## 打包 

```bash
mvn clean package
```

## 部署

### 服务端部署

1) 将sp-server jar包上传服务器
2) 启动 `java -jar sp-server.jar 9999`

### 客户端部署
直接启动sp-client.jar
> 参数指定方式，包括vm方式 `-Dlocal=local:port -Dremote=server:port`  
> 或者直接 `java -jar sp-client.jar local=local:port remote=server:port`


> 注意： 最好不要进过http代理(类似`nginx`),可能会出现一些问题。