# 技术架构
数据挖掘图数据平台底层基于 Apache HugeGraph 针对图载入、图存储、图计算、图可视化等多个模块做了二次开发，旨在提升易用性、安全稳定性以及读写性能，目前主要支撑人才图谱、产业链图谱、数据血缘等应用，包括秒级实时更新，亿级数据量的批量写入与毫秒级查询等场景。由组内 Apache Committer/Contributor 负责系统架构和功能的设计与研发。后续计划集成 GraphScope 增强图分析算法和图机器学习方面的能力。

<div align=center><img width = '900' height ='888' src ="/img.png"/></div>


**各模块说明**  
- **图载入**模块目前支持从多种异构数据源多种方式地载入图数据，包括本地文件，关系型数据库，HDFS 文件(parquet/orc)。社区单机版 hugegraph-loader 
功能有限，基于实际场景，我们对关系型数据库数据源支持SQL做简单的图数据预处理逻辑，自研了 hugegraph-spark-loader 和 hugegraph-flinkcdc-loader 
分别用于亿级别图数据的批量预处理和载入和实时图数据的增删改操作。后续计划支持 bypass-server loader ，即跳过server端直接与底层存储交互，可大大提高海量数据的载入效率。
- **图存储**模块基于开源版的 Apache HugeGraph 做了二次开发，包括支持动态创建多图实例；ID中增加2byte作为分区位用于底层存储的预分区；improve 
dockerFile；增加step的执行策略避免label不存在是hasLabel造成的全局遍历；扩展接口集成toolchain的工具以及一些异常的 bugfix 
  等等，后续计划会继续针对多跳的查询场景做优化，以及根据实际业务场景对图缓存做扩展，另外还会和社区合作完成基于 raft 协议分布式架构下 rocksdb 的数据分片和数据一致性保证等。
- **图查询**模块支持 Read Committed 级别事务，支持标准的 Gremlin 
语法查询，以及封装了常见的图分析计算算法（针对子图），提供了可视化查询工具GraphStudio，客户端支持RestAPI与Java Clinet，python client 正在研发中。
- **图计算**（OLAP）模块针对全图，集成了两个组件，原生集成了 hugegraph-computer ，对 Spark GraphX 
的支持在计划中，目前支持常用的图算法包括PageRank、社区发现、最短路径等等。
- **图学习**模块目前还在探索调研中，计划集成 GraphScope，GraphScope 提供了一个图学习算法的统一编程框架，支持常见图学习算法的开发，包括 GNNs, 
知识图谱模型，图嵌入算法等，并且和主流的深度学习算法兼容，包括TensorFlow 和PyTorch。
- 目前图平台主要应用在知识图谱、搜索推荐、数据血缘等多个场景的多个业务中。

**对 Apache HugeGraph 的部分贡献**
- [[Feature] Enable dynamic create/drop graph #1809](https://github.com/apache/incubator-hugegraph/pull/1809)
- [[Feature][Umbrella] Support spark for hugegraph-loader module #283](https://github.com/apache/incubator-hugegraph-toolchain/issues/283)  
- [[Feature] Introduce HugeGraphFlinkCDCLoader #290](https://github.com/apache/incubator-hugegraph-toolchain/issues/290)  
- [[Feature] Support custom sql for hugegraph-loader module #262](https://github.com/apache/incubator-hugegraph-toolchain/issues/262)  
- [More...](https://github.com/apache/incubator-hugegraph-toolchain/pulls?q=is%3Apr+is%3Aclosed+author%3Asimon824)

# 关于图数据库
## 应用场景
- **知识图谱**：知识图谱是结构化的语义知识库，用于以符号形式描述物理世界中的概念及其相互关系。 
其基本组成单位是“实体-关系-实体”三元组，以及实体及其相关属性-值对，实体间通过关系相互联结，构成网状的知识结构。知识图谱能够把领域内复杂知识通过信息抽取、数据挖掘、语音匹配、语义计算、知识推理等过程精确地描述出来，并且可以描述知识的演化过程和发展规律，从而为研究和决策提供准确、可追踪、可解释、可推理的知识数据。
- **搜索推荐**：基于知识图谱的搜索推荐主要是通过实体与实体之间的关系，将用户搜索实体的相关内容根据一定的逻辑推荐给用户，
以清华大学为例，搜索“五道口职业技术学校”后，会关联上“清华大学”，最终出现“清华大学”相关的搜索结果，同时“知名校友”，“相关院校”的推荐栏目，通过知识图谱，能够准确的知道哪些人从清华毕业的，然后通过一系列的热点排序算法，将用户最关心的毕业生选出来。
- **智能问答**：知识图谱可作为智能问答的知识基础，提高问答精准度，省去一个个知识点构建的过程，降低知识构建的成本。 同时扩大问答场景的适应，提升机器人与用户对话的能力。 
基于用户的行为路径和用户信息构建的业务知识图谱，给用户推荐给可能感兴趣的内容，提高推荐结果的相关性。 该领域重在关系的发现，找到关系网络。
- **安全风控**：业务部门有内容风控的需求，希望在商户、用户、评论中通过多跳查询来识别虚假评价；在支付时进行金融风控的验证，实时多跳查询风险点。
- **链路分析**：包括代码分析、服务治理、数据血缘管理，比如公司数据平台上有很多 ETL Job，Job 和 Job 之间存在强弱依赖关系，这些强弱依赖关系形成了一张图，在进行 ETL Job 
的优化或者故障处理时，需要对这个图进行实时查询分析。
- **社交网络**：通过社交网络的知识图谱发现更多的社会关系，服务于社交类软件。
- **组织架构**： 公司组织架构的管理，实线汇报链、虚线汇报链、虚拟组织的管理，以及商家连锁门店的管理。比如，维护一个商家在不同区域都有哪些门店，能够进行多层关系查找或者逆向关系搜索。
## 关系型数据库 VS 图数据库  
### 1.应用实例 
**张三的好友所在的公司有多少员工？**
- **关系型数据库的解决方案**  
<table>
    <tr>
        <td colspan="2"><strong>1.公司信息表（Companies）</strong></td>
        <td colspan="2"><strong>2.员工信息表（Employees）</strong></td>
        <td colspan="2"><strong>3.雇佣关系表（CompanyRelations）</strong></td>
        <td colspan="2"><strong>4.好友关系表（FriendRelations）</strong></td>
    <tr>
    <tr>
        <td>id</td>
        <td>自增id</td>
        <td>id</td>
        <td>自增id</td>
        <td>id</td>
        <td>自增id</td>
        <td>id</td>
        <td>自增id，FromId 好友关系的一方</td>
    <tr>
    <tr>
        <td>name</td>
        <td>公司名称</td>
        <td>name</td>
        <td>姓名</td>
        <td>CompanyForeignKey</td>
        <td>公司id</td>
        <td>Toid</td>
        <td>好友关系的另一方</td>
    <tr>
    <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td>EmployeeForeignKey</td>
        <td>员工id</td>
        <td></td>
        <td></td>
    <tr>
</table>

```sql
SELECT Companies.Name, COUNT(CompanyRelations.CompanyForeignKey)
    FROM Employees AS e1
    JOIN FriendRelations ON FriendRelations.FromId = e1.Id
    JOIN Employees AS e2 ON e2.Id = FriendRelations.ToId
    JOIN CompanyRelations ON e2.Id = CompanyRelations.EmployeeForeignKey
    JOIN Companies ON Companies.id = CompanyRelations.CompanyForeignKey
    WHERE e1.Name = '张三' GROUP BY CompanyRelations.CompanyForeignKey
```

- **图数据库的解决方案**  
<div align=center><img src ="/intro2.png"/></div>  

```java
g.V().has('name','张三').out('friend')
        .out('employed').in('employed').grouCount()
```

### 2.效率
如图，对传统关系型数据库跟图数据库的查询性能对比，在一个包含 100 万人、每人约有 50 个朋友的社交网络里找最大深度为 5 的朋友的朋友，实验结果表明多跳查询中图数据库优势明显。  
<div align=center><img src ="/img_2.png"/></div>

### 3.趋势
「DB-Engines 排名」是按流行程度对数据库管理系统进行排名，涵盖 380 多个系统，每月更新一次。 排名标准包括搜索数据库名称时的搜索引擎结果的数量、Google 趋势、Stack 
Overflow、社交网络和提及数据库的工作机会等数据，综合比较排名。  
[DBMS popularity broken down by database model](https://db-engines.com/en/ranking_categories)  
<div align=center><img src ="/img_1.png"/></div>

