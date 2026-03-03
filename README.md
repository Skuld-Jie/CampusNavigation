# 数据结构课程设计

## 技术栈

Spring + SpringBoot +MyBatis-Plus 

**(使用图的Dijkstra算法)**

## 使用方法

### 1. 替换 `index.html` 中的百度API

![image-20260303094710647](https://cdn.jsdelivr.net/gh/Skuld-Jie/First-cup@main/notebook-pictures/Class/image-20260303094710647.png)

#### 1.1 进入百度地图开放平台

链接：[百度地图-百万开发者首选的地图服务商,提供专属的行业解决方案](https://lbsyun.baidu.com/)

登录后进入控制台

点击 “应用管理” 

点击 “我的应用” 

点击 “创建应用” 

输入 “应用名称”

“应用类型” 选择 “浏览器端”

![image-20260303094119373](https://cdn.jsdelivr.net/gh/Skuld-Jie/First-cup@main/notebook-pictures/Class/image-20260303094119373.png)

白名单 标记为 `*`

![image-20260303101619875](https://cdn.jsdelivr.net/gh/Skuld-Jie/First-cup@main/notebook-pictures/Class/image-20260303101619875.png)

提交后 获得 AK ， 替换 `index.html` 中的 AK即可

![image-20260303095629742](https://cdn.jsdelivr.net/gh/Skuld-Jie/First-cup@main/notebook-pictures/Class/image-20260303095629742.png)

<font color="red">注意：此时白名单限制过低，仅限于本地运行，公开访问会有安全隐患，AK 也不要公开，我的AK已经作废（无法使用）所以公开了</font>

### 2. 替换 ` application.yml` 中的 datasource

```yml
url: jdbc:mysql://localhost:3306/campus?serverTimezone=Asia/Shanghai
username: root
password: 6789
```

campus 要替换你的数据库名称

password 要替换你的

**同时需要根据你的地图范围 建立数据库**



















