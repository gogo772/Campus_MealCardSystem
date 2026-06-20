# 校园饭卡管理系统

## 项目简介

校园饭卡管理系统是一套针对大学校园食堂交费和一般消费等方面的信息管理系统，包括学生或教职工在校内消费的各方面内容：刷卡消费、查询、充值和持卡者信息管理等。

本项目为 **Spring Boot 2.x + Thymeleaf + MyBatis-Plus** 的单体 Web 应用，提供完整的 Web UI 界面与 RESTful API。

---

## 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 后端主框架 |
| MyBatis-Plus | 3.5.3.1 | ORM 框架 |
| Druid | 1.2.20 | 阿里巴巴数据库连接池 |
| MySQL Connector | 8.0.33 | MySQL JDBC 驱动 |
| Spring Security | 5.x（内置） | 认证与授权框架 |
| Thymeleaf | 3.x（内置） | 服务端模板引擎 |
| thymeleaf-extras-springsecurity5 |  — | Thymeleaf Security 方言（前端权限标签） |
| Lombok | 1.18.30 | 简化 Java Bean 代码 |
| MySQL | 8.0+ | 数据库 |

---

## 项目结构

```
Campus_MealCardSystem/
├── src/main/java/com/example/campus_mealcardsystem/
│   ├── common/
│   │   └── Result.java                    # 统一 API 返回结果封装
│   ├── config/
│   │   ├── MyMetaObjectHandler.java       # MyBatis-Plus 自动填充（create_time/update_time）
│   │   └── SecurityConfig.java           # Spring Security 安全配置
│   ├── controller/
│   │   ├── BackupController.java          # 数据备份接口
│   │   ├── CardHolderController.java      # 持卡者管理 API
│   │   ├── LoginController.java           # 登录/登出
│   │   ├── MyCardController.java          # 我的饭卡页面接口
│   │   ├── PageController.java            # 页面路由（Thymeleaf 视图）
│   │   └── TransactionController.java     # 交易记录 API
│   ├── entity/
│   │   ├── CardHolder.java                # 持卡者实体
│   │   ├── SysUser.java                   # 系统用户实体
│   │   └── TransactionRecord.java        # 交易记录实体
│   ├── exception/
│   │   ├── BusinessException.java         # 自定义业务异常
│   │   └── GlobalExceptionHandler.java   # 全局异常处理器
│   ├── mapper/
│   │   ├── CardHolderMapper.java          # 持卡者 Mapper
│   │   ├── SysUserMapper.java             # 系统用户 Mapper
│   │   └── TransactionRecordMapper.java   # 交易记录 Mapper
│   ├── security/
│   │   ├── SysUserDetails.java            # Spring Security 用户详情实现
│   │   └── SysUserDetailsService.java     # 用户认证逻辑
│   ├── service/
│   │   ├── BackupService.java             # 数据备份服务接口
│   │   ├── BaseService.java               # 通用 Service 接口
│   │   ├── CardHolderService.java         # 持卡者服务接口
│   │   └── TransactionRecordService.java  # 交易记录服务接口
│   ├── service/impl/
│   │   ├── BackupServiceImpl.java         # 数据备份服务实现
│   │   ├── BaseServiceImpl.java           # 通用 Service 实现
│   │   ├── CardHolderServiceImpl.java     # 持卡者服务实现
│   │   └── TransactionRecordServiceImpl.java # 交易记录服务实现
│   └── CampusMealCardSystemApplication.java # Spring Boot 启动类
│
├── src/main/resources/
│   ├── application.yml                    # 应用配置文件（YAML 格式）
│   ├── static/css/style.css              # 全局样式
│   └── templates/                        # Thymeleaf 模板
│       ├── 403.html                      # 无权限提示页
│       ├── index.html                     # 首页（仪表盘）
│       ├── login.html                     # 登录页
│       ├── loss-management.html           # 挂失管理页
│       ├── my-card.html                   # 我的饭卡页
│       ├── recharge-consume.html          # 充值/消费页
│       ├── register.html                  # 注册新卡页
│       ├── settings.html                  # 系统设置页
│       ├── transactions.html              # 交易记录页
│       └── fragments/                    # 公共片段
│           ├── head.html                  # <head> 公共部分
│           ├── sidebar.html               # 侧边栏导航
│           └── topbar.html                # 顶部导航栏
│
├── sql/
│   └── init.sql                          # 数据库初始化脚本
├── docs/
│   └── 代码规范.md                       # 代码规范文档
├── pom.xml                                # Maven 配置
└── README.md                             # 本文档
```

---

## 功能模块

### 1. 用户认证与授权
- 基于 Spring Security 的登录/登出
- 角色权限控制（`ADMIN` / `STUDENT` / `TEACHER`）
- 前端菜单根据角色动态显示（`sec:authorize` 标签）

### 2. 持卡者管理
- 注册（新增持卡者）
- 查询持卡者信息（按 ID / 饭卡号 / 全部）
- 编辑持卡者信息
- 注销（删除持卡者）

### 3. 饭卡业务
- 充值
- 消费
- 挂失 / 解冻

### 4. 交易记录
- 查询交易记录（按饭卡号、时间范围）
- 交易明细查看

### 5. 系统管理
- 数据备份（导出 SQL 文件，存储于 `backup/` 目录）
- 系统设置

---

## 数据库设计

初始化脚本：`sql/init.sql`

### 数据表

| 表名 | 说明 |
|------|------|
| `card_holder` | 持卡者信息（饭卡号、姓名、类型、余额、状态） |
| `transaction_record` | 交易记录（交易类型、金额、前后余额、时间） |
| `sys_user` | 系统用户（登录账号、密码 BCrypt 加密、角色、绑定饭卡号） |

### 预置测试账号

| 用户名 | 密码 | 角色 | 绑定饭卡 |
|--------|------|------|----------|
| `admin` | `123456` | ADMIN | 无 |
| `zhangsan` | `1234` | STUDENT | 20240001 |
| `lisi` | `1234` | STUDENT | 20240002 |
| `wangwu` | `1234` | TEACHER | 20240003 |

> ⚠️ **注意**：密码使用 BCrypt（`$2a$` 格式）加密存储，请勿直接修改数据库中的密码字段。

---

## 快速开始

### 1. 环境准备

| 环境 | 版本要求 |
|------|-----------|
| JDK | 17+ |
| Maven | 3.6+ |
| MySQL | 8.0+ |

### 2. 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 在 MySQL 客户端中执行初始化脚本
source sql/init.sql
```

或直接复制 `sql/init.sql` 中的 SQL 语句在 MySQL 客户端（如 Navicat、MySQL Workbench）中执行。

### 3. 修改配置文件

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/campus_card_system?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8
    username: root          # 修改为你的 MySQL 用户名
    password: 123456        # 修改为你的 MySQL 密码
```

> ⚠️ **重要**：默认密码为 `123456`，请根据实际环境修改，否则应用将无法启动。

### 4. 编译和运行

```bash
# 使用 Maven Wrapper（推荐，无需本地安装 Maven）
./mvnw clean spring-boot:run          # Linux / macOS
mvnw.cmd clean spring-boot:run         # Windows

# 或本地已安装 Maven
mvn clean spring-boot:run

# 打包后运行
mvn clean package
java -jar target/Campus_MealCardSystem-0.0.1-SNAPSHOT.jar
```

### 5. 访问应用

- 应用启动后访问：**http://localhost:8080**
- 默认自动跳转至登录页 `/login`
- 登录成功后出现侧边栏，地址栏实际路径为 `/my-card`（我的饭卡）

---

## API 接口文档

### 持卡者接口 `/api/card-holder`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/card-holder` | 注册（新增持卡者） |
| GET | `/api/card-holder/{id}` | 根据 ID 查询 |
| GET | `/api/card-holder/card-number/{cardNumber}` | 根据饭卡号查询 |
| GET | `/api/card-holder` | 查询所有持卡者 |
| POST | `/api/card-holder/recharge` | 充值 |
| POST | `/api/card-holder/consume` | 消费 |
| POST | `/api/card-holder/report-loss/{cardNumber}` | 挂失 |
| POST | `/api/card-holder/unfreeze/{cardNumber}` | 解冻 |
| PUT | `/api/card-holder` | 更新持卡者信息 |
| DELETE | `/api/card-holder/{id}` | 注销（删除） |

### 交易记录接口 `/api/transaction`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/transaction/card-number/{cardNumber}` | 根据饭卡号查询 |
| GET | `/api/transaction` | 查询所有交易记录 |
| DELETE | `/api/transaction/{id}` | 删除交易记录 |

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET/POST | `/login` | 登录页面 / 登录提交 |
| POST | `/logout` | 登出 |

---

## 统一返回格式

所有 API 接口返回统一的结果格式：

```json
{
  "code": 200,
  "msg": "success",
  "data": { ... }
}
```

### 状态码说明

| code | 说明 |
|------|------|
| 200 | 成功 |
| 400 | 参数错误 / 业务校验失败 |
| 401 | 未认证（未登录） |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 配置说明

关键配置项（`application.yml`）：

```yaml
# 服务端口（默认 8080）
server:
  port: 8080

# 备份文件存储目录（相对于项目根目录）
app:
  backup:
    dir: backup

# 日志级别（开发环境建议 debug）
logging:
  level:
    com.example.campus_mealcardsystem: debug
```

---

## 代码规范

请参考 `docs/代码规范.md` 文件，了解项目的代码规范和最佳实践。

---

## 注意事项

1. **数据库配置**：请根据实际环境修改 `application.yml` 中的数据库连接配置
2. **端口配置**：应用默认使用 `8080` 端口，可在 `application.yml` 中修改
3. **密码加密**：系统用户密码使用 BCrypt 加密，`$2a$` 格式；若手动向 `sys_user` 表插入数据，务必使用 BCrypt 加密密码
4. **日志级别**：开发环境可使用 `debug` 级别，生产环境建议使用 `info` 或 `warn` 级别
5. **事务管理**：涉及数据修改的操作都进行了事务管理
6. **异常处理**：所有异常都通过 `GlobalExceptionHandler` 进行统一处理，返回统一的错误格式
7. **MyBatis-Plus 自动填充**：`create_time` 和 `update_time` 由 `MyMetaObjectHandler` 自动填充，无需手动设置

---

## 后续开发计划

- [x] 添加用户认证和授权（✅ 已完成 - Spring Security）
- [x] 添加前端页面（✅ 已完成 - Thymeleaf）
- [ ] 添加数据统计和报表功能（消费趋势图、月度报表）
- [ ] 添加操作日志功能
- [ ] 添加批量导入持卡者功能（Excel 导入）
- [ ] 支持多食堂 / 多消费点管理
- [ ] 密码修改功能（用户自行修改）

---

## 联系方式

如有问题，请联系项目管理员。
