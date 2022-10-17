# 关于图数据库


# 图平台技术架构
数据挖掘图数据平台底层基于 Apache HugeGraph 针对图载入、图存储、图计算、图可视化等多个模块做了二次开发，旨在提升易用性、安全稳定性以及读写性能，目前主要支撑人才图谱、产业链图谱、数据血缘等应用，包括秒级实时更新，亿级数据量的批量写入与毫秒级查询等场景。由组内 Apache Committer/Contributor 负责系统架构和功能的设计与研发。后续计划集成 GraphScope 增强图分析算法和图机器学习方面的能力。

![](/img.png)

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
- [[Feature][Umbrella] Support spark for hugegraph-loader module #283](https://github.com/apache/incubator-hugegraph-toolchain/issues/283)  
- [[Feature] Introduce HugeGraphFlinkCDCLoader #290](https://github.com/apache/incubator-hugegraph-toolchain/issues/290)  
- [[Feature] Support custom sql for hugegraph-loader module #262](https://github.com/apache/incubator-hugegraph-toolchain/issues/262)  
- [More...](https://github.com/apache/incubator-hugegraph-toolchain/pulls?q=is%3Apr+is%3Aclosed+author%3Asimon824)