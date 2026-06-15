# 校园饭卡管理系统

## 项目简介

校园饭卡管理系统是一套针对大学校园食堂交费和一般消费等方面的信息管理系统，包括学生或教职工在校内消费的各方面内容：刷卡消费、查询、充值和持卡者信息管理等。

## 技术栈

- **后端框架**：Spring Boot 4.1.0
- **ORM框架**：MyBatis-Plus 3.5.7
- **数据库连接池**：Druid 1.2.23
- **数据库**：MySQL 8.0+
- **简化代码**：Lombok
- **模板引擎**：Thymeleaf

## 项目结构

```
Campus_MealCardSystem/
├── src/main/java/com/example/campus_mealcardsystem/
│   ├── common/                    # 通用类
│   │   └── Result.java           # 统一返回结果类
│   ├── config/                    # 配置类
│   ├── controller/                # 控制器层
│   │   ├── CardHolderController.java
│   │   └── TransactionController.java
│   ├── entity/                   # 实体类
│   │   ├── CardHolder.java
│   │   └── TransactionRecord.java
│   ├── mapper/                   # Mapper接口层
│   │   ├── CardHolderMapper.java
│   │   └── TransactionRecordMapper.java
│   ├── service/                  # 服务接口层
│   │   ├── BaseService.java
│   │   ├── CardHolderService.java
│   │   └── TransactionRecordService.java
│   ├── service/impl/            # 服务实现层
│   │   ├── BaseServiceImpl.java
│   │   ├── CardHolderServiceImpl.java
│   │   └── TransactionRecordServiceImpl.java
│   └── CampusMealCardSystemApplication.java  # 启动类
├── src/main/resources/
│   ├── application.properties     # 应用配置文件
│   ├── static/                   # 静态资源
│   └── templates/                # 模板文件
├── sql/
│   └── init.sql                  # 数据库初始化脚本
├── docs/
│   └── 代码规范.md              # 代码规范文档
├── pom.xml                       # Maven配置文件
└── README.md                     # 项目说明文档
```

## 功能模块

### 1. 持卡者管理
- 注册（新增持卡者）
- 查询持卡者信息
- 注销（删除持卡者）

### 2. 饭卡管理
- 充值
- 消费
- 挂失
- 解冻

### 3. 交易记录管理
- 创建交易记录
- 查询交易记录

## 快速开始

### 1. 环境准备

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库初始化

1. 登录MySQL：`mysql -u root -p`
2. 执行初始化脚本：`source sql/init.sql`
3. 或者直接在MySQL客户端中执行`sql/init.sql`文件中的SQL语句

### 3. 修改配置文件

修改`src/main/resources/application.properties`文件中的数据库连接配置：
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. 编译和运行

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/Campus_MealCardSystem-0.0.1-SNAPSHOT.jar
```

### 5. 访问应用

- 应用启动后，访问：http://localhost:8080
- API接口前缀：`/api`

## API接口文档

### 持卡者接口

- `POST /api/card-holder` - 注册（新增持卡者）
- `GET /api/card-holder/{id}` - 根据ID查询
- `GET /api/card-holder/card-number/{cardNumber}` - 根据饭卡号查询
- `GET /api/card-holder` - 查询所有持卡者
- `POST /api/card-holder/recharge` - 充值
- `POST /api/card-holder/consume` - 消费
- `POST /api/card-holder/report-loss/{cardNumber}` - 挂失
- `POST /api/card-holder/unfreeze/{cardNumber}` - 解冻
- `DELETE /api/card-holder/{id}` - 注销（删除）

### 交易记录接口

- `POST /api/transaction` - 创建交易记录
- `GET /api/transaction/{id}` - 根据ID查询
- `GET /api/transaction/card-number/{cardNumber}` - 根据饭卡号查询
- `GET /api/transaction` - 查询所有交易记录
- `DELETE /api/transaction/{id}` - 删除交易记录

## 统一返回格式

所有API接口返回统一的结果格式：

```json
{
  "code": 200,
  "msg": "success",
  "data": {...}
}
```

### 状态码说明

- `200` - 成功
- `400` - 参数错误
- `404` - 资源不存在
- `500` - 服务器内部错误

## 代码规范

请参考`docs/代码规范.md`文件，了解项目的代码规范和最佳实践。

## 注意事项

1. 数据库配置：请根据实际环境修改数据库连接配置
2. 端口配置：应用默认使用8080端口，可在配置文件中修改
3. 日志级别：开发环境可使用debug级别，生产环境建议使用info或warn级别
4. 事务管理：涉及数据修改的操作都进行了事务管理
5. 异常处理：所有异常都进行了统一处理，返回统一的错误格式

## 后续开发计划

- [ ] 添加用户认证和授权
- [ ] 添加前端页面
- [ ] 添加数据统计和报表功能
- [ ] 添加数据备份和恢复功能
- [ ] 添加操作日志功能
- [ ] 添加权限管理功能

## 联系方式

如有问题，请联系项目管理员。
