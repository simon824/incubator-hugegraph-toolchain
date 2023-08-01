# 技术架构
数据挖掘图数据平台底层主要基于 Apache HugeGraph 实现。
- 包括针对 HugeGraph 的图载入、图存储、图计算、图可视化等多个模块做了二次开发，提高稳定性、易用性、安全性和读写性能。
- 自研分布式版本，支持多分区多副本，可自由扩缩容。
- 自研 HugeGraph AI 模块，包括支持向量检索，与 Langchain 集成辅助 LLM 构建私有知识库问答系统、与 GNN 的集成等。

目前主要支撑人才图谱、产业链图谱、个性化题目推荐、数据血缘等应用，包括秒级实时更新，亿级数据量的批量写入与毫秒级查询等场景。由组内多位 Apache Committer/Contributor 
负责系统架构和功能的设计与研发。

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

