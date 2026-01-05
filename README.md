
# Smart Energy Backend（智慧校园能耗监测平台后端）

## 1. 项目简介

本项目为“智慧校园能耗监测与管理平台”后端服务，基于 **Spring Boot** 构建，提供建筑管理、设备管理、能耗采集与模拟、告警管理、统计分析以及登录鉴权等 RESTful API。系统采用 **Controller-Service-DAO** 分层架构，使用 **JWT + Spring Security** 实现权限控制。

---

## 2. 技术栈

* Spring Boot
* Spring MVC
* Spring Security + JWT
* JPA / MySQL
* Knife4j（接口文档）

---

## 3. 项目结构

```
src/main/java/ynu/edu/smart_energy
├── common        # ApiResponse、异常类、全局异常处理
├── config        # Knife4j、ModelMapper、WebMvc 等配置
├── controller    # REST API 控制层
├── dao           # DAO/Repository 数据访问层
├── dto           # DTO 数据传输对象
├── entity        # Entity 实体类（映射数据库表）
├── mapper        # Entity <-> DTO 转换
├── security      # Security 配置 + JWT 过滤器
├── service       # 业务接口 + 实现 + simulator（模拟采集）
└── SmartEnergyApplication.java
```

---

## 4. 环境要求

* JDK 17
* Maven 3.6+
* MySQL 8.0+
* IDE：IntelliJ IDEA

---

## 5. 配置说明

### 5.1 数据库配置

数据库脚本在根目录的 energy_20231120056.sql 中

修改 `src/main/resources/application.yml` 中数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/energy_20231120056?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 自己的密码
```

数据库命名：`energy_20231120056`
启动前请确保已创建数据库，并完成建表（可用项目 SQL 脚本或 JPA 自动建表）。

---

## 6. 启动方式

### 6.1 使用 IDE 启动

运行：
`src/main/java/ynu/edu/smart_energy/SmartEnergyApplication.java`

### 6.2 使用命令启动

```bash
mvn spring-boot:run
```

启动成功后默认访问地址：

* 后端服务：[http://localhost:9090/energy](http://localhost:9090/energy)
* Knife4j 文档：[http://localhost:9090/energy/doc.html](http://localhost:9090/energy/doc.html)

---

## 7. 核心功能模块

* 认证登录：JWT 鉴权、角色权限（ADMIN / USER）
* 建筑管理：增删改查
* 设备管理：增删改查、设备 SN 唯一性校验、绑定建筑/房间
* 能耗采集模拟：定时生成电压/电流/功率/累计电量并入库
* 告警模块：过载/电压异常自动检测并生成告警记录
* 统计分析：建筑能耗排行、设备功率趋势、告警统计等

---

## 8. 常见问题

1. **数据库连接失败**
   检查 MySQL 是否启动、账号密码是否正确、库名是否存在。

2. **前端跨域问题**
   确认 `WebMvcConfig` 或跨域配置是否开启。

3. **接口返回 401/403**
   确认请求头是否携带 `Authorization`（JWT Token）。

---

## 9. License

仅用于课程学习与作业提交。

---
