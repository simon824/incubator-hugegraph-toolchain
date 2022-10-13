![](/load.png)  
目前支持两种方式的图载入，“**通过 Loader 载图**”和“**通过 Studio 载图**”

1. **通过 Loader 载图**的方式比较灵活，适合大规模、复杂场景的图载入，支持自定义 ETL 逻辑，支持 Spark 亿级数据的批量写入，支持 FlinkCDC 实时图数据更新。
2. **Studio 界面载图**的方式适合 schema 简单、规模小的图载入。

## 通过 Loader 载图（推荐）
图载入操作首先需要准备两个文件 “**图Schema文件**” 和 “**数据源映射文件**”。
### 1. 定义图Schema
可参考[人才图谱配置](https://dolphinscheduler.research-pro.sy.cvte.cn/dolphinscheduler/ui/resource/file/list/9)

```java
/**
1. 属性（包括点和边的所有属性key）
    - 注意属性值的类型定义，支持：asText/asInt/asDate/asUUID/asBoolean/asByte/asBlob/asDouble/asFloat/asLong
**/

schema.propertyKey("name").asText().ifNotExist().create();
schema.propertyKey("create_time").asText().ifNotExist().create();

/**
2. 点标签 （实体的名称）
    - 指定主键（主键无法被检索）: `.primaryKeys("c_company_uid")`
    - 指定可以为空的属性: `.nullableKeys("company_uid","company_name")` 
    - ID 策略: useCustomizeStringId(自定义String类型【推荐】)、useAutomaticId(自动生成)、usePrimaryKeyId(使用主键)、useCustomizeNumberId(自定义int类型)
**/
schema.vertexLabel("company").useCustomizeStringId().properties("name","create_time").ifNotExist().create();
schema.vertexLabel("school").useCustomizeStringId().properties("name").ifNotExist().create();

/**
3. 边标签（关系的名称）
**/
schema.edgeLabel("employ").sourceLabel("talent").targetLabel("company").properties("name","create_time").ifNotExist().create();
schema.edgeLabel("study").sourceLabel("talent").targetLabel("school").properties("name").ifNotExist().create();

/**
 4. 索引 （不确定则联系 @zhangshiming）
    - 索引类型：
        - secondary: 支持精确匹配的二级索引，允许建立联合索引，联合索引支持索引前缀搜索
        - range: 支持数值类型的范围查询
        - searcL: 支持全文检索的索引
        - shard: 支持前缀匹配 + 数字范围查询的索引
        - unique: 支持属性值唯一性约束，即可以限定属性的值不重复，允许联合索引，但不支持查询
**/
schema.indexLabel("companyIndex").onV("company").by("c_company_name").secondary().ifNotExist().create();
schema.indexLabel("schoolIndex").onV("school").by("c_school_name").secondary().ifNotExist().create();
```

### 2. 定义数据源映射
可参考[人才图谱配置](https://dolphinscheduler.research-pro.sy.cvte.cn/dolphinscheduler/ui/resource/file/list/10)
- **Mysql 输入配置**  
  - type: 输入源类型，必须填 jdbc 或 JDBC，必填；
  - vendor: 数据库类型，可选项为 [MySQL、PostgreSQL、Oracle、SQLServer]，不区分大小写，必填；
  - driver: jdbc 使用的 driver 类型，必填；
  - url: jdbc 要连接的数据库的 url，必填；
  - database: 要连接的数据库名，必填；
  - schema: 要连接的 schema 名，不同的数据库要求不一样，下面详细说明；
  - table: 要连接的表名，custom_sql 和 table 参数必须填其中一个；
  - custom_sql: 自定义 SQL 语句，custom_sql 和 table 参数必须填其中一个；
  - username: 连接数据库的用户名，必填；
  - password: 连接数据库的密码，必填；
  - batch_size: 按页获取表数据时的一页的大小，默认为 500，选填；
- **HugeGraph 输出配置**  
  - **通用配置**  
    - label: 待导入的顶点/边数据所属的label，必填；
    - custom_sql: 自定义SQL逻辑；
    - field_mapping: 将输入源列的列名映射为顶点/边的属性名，选填；
    - value_mapping: 将输入源的数据值映射为顶点/边的属性值，选填；
    - selected: 选择某些列插入，其他未选中的不插入，不能与ignored同时存在，选填；
    - ignored: 忽略某些列，使其不参与插入，不能与selected同时存在，选填；
    - null_values: 可以指定一些字符串代表空值，比如"NULL”，如果该列对应的顶点/边属性又是一个可空属性，那在构造顶点/边时不会设置该属性的值，选填；
    - update_strategies: 如果数据需要按特定方式批量**更新**时可以对每个属性指定具体的更新策略 (具体见下)，选填；
    - unfold: 是否将列展开，展开的每一列都会与其他列一起组成一行，相当于是展开成了多行；比如文件的某一列（id 列）的值是[1,2,3]，其他列的值是18,Beijing，当设置了 unfold 之后，这一行就会变成 3 行，分别是：1,18,Beijing，2,18,Beijing和3,18,Beijing。需要注意的是此项只会展开被选作为 id 的列。默认 false，选填；
- **顶点映射特有的配置**
  - id: 指定某一列作为顶点的 id 列，当顶点 id 策略为CUSTOMIZE时，必填；当 id 策略为PRIMARY_KEY时，必须为空；
- **边映射特有的配置**
  - source: 选择输入源某几列作为**源顶点**的 id 列，当源顶点的 id 策略为 CUSTOMIZE时，必须指定某一列作为顶点的 id 列；当源顶点的 id 策略为 PRIMARY_KEY时，必须指定一列或多列用于拼接生成顶点的 id，也就是说，不管是哪种 id 策略，此项必填；
  - target: 指定某几列作为**目标顶点**的 id 列，与 source 类似，不再赘述；
  - unfold_source: 是否展开文件的 source 列，效果与顶点映射中的类似，不再赘述；
  - unfold_target: 是否展开文件的 target 列，效果与顶点映射中的类似，不再赘述；

### 3. 初始化图实例&配置可视化
> 权限问题联系 @张世鸣 @刁汉财 @陈丽旋

**3.1. 创建配置文件**  
定义完上述“图schema”和“数据源映射”两个文件后，如下图，在 [DolphinScheduler](https://dolphinscheduler.research-pro.sy.cvte.cn/dolphinscheduler/ui/resource/file/subdirectory/7)目录下创建 ${graph_name} 文件夹，并创建以 ${graph_name} 命名的这两个文件，json（数据源映射）和 sh（图schema）。（其中 ${graph_name} 为**全局唯一**的图实例名，）
![](/load1.png)

**3.2. 配置初始化任务**<br />在 [DolphinScheduler 图初始化工作流](https://dolphinscheduler.research-pro.sy.cvte.cn/dolphinscheduler/ui/projects/5879643923328/workflow-definition)中，随意选择一个“图数据初始化模版”，进入任务定义页面后直接点击保存，会出现弹窗，编辑如图所示内容，完成后点击添加。
- GRAPH_NAME：图实例名称
- USER_NAME：用户名（自定义）
- PASSWORD：密码（自定义）

![](/load2.png)  

**3.3. 执行初始化任务**：回到工作流定义页面找到你修改过的工作流，点击上线，然后点击运行。最后可以在任务实例查看到任务执行日志。<br />**3.4. Hubble 可视化配置**

- 图ID可定义为图的中文名
- 图名称为步骤 3.1 中定义的 ${graph_name}
- 主机名/和端口号可直接复制其他图。
- 用户名密码为步骤 3.2 中在DS定义的USER_NAME和PASSWORD

![](/load3.png)

-----

## 通过 Hubble 载图（不推荐）

- **定义图 Schema**

![](/load4.png)

- **定义数据源映射（只支持csv且单个文件不大于1G）**

![](/load5.png)
