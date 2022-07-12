# hugegraph-toolchain

1. 增加两个配置
    - **dm_page**: 是否翻页，默认true为翻页，如果表没有自增的唯一id，则不能做自动翻页，需要把dm_page配置为false，特别是自定义sql的场景要注意sql
    中是否有 select 唯一键，没有的话需要将 dm_page 配置为false。
    - **dm_dm_kv_attributes**: 是否开启属性和属性值以建值对方式存储在两个数据库字段中  
        比如：表 attributes 有字段 field_a、field_b；field_a 的值为所有的属性键，field_b 的值为所有的属性值。



---
`hugegraph-toolchain` is the integration project of a series of utilities for [HugeGraph](https://github.com/hugegraph/hugegraph), it includes 4 main modules.

## Modules

- [hugegraph-loader](./hugegraph-loader): Loading datasets into the HugeGraph from multiple data sources.
- [hugegraph-hubble](./hugegraph-hubble): Online HugeGraph management and analysis dashboard (Include: data loading, schema management, graph traverser and display)
- [hugegraph-tools](./hugegraph-tools): Command line tool for deploying, managing and backing-up/restoring graphs from HugeGraph.
- [hugegraph-client](./hugegraph-client): A Java-written client for HugeGraph, providing `RESTful` APIs for accessing graph vertex/edge/schema/gremlin/variables and traversals etc.

## Doc

The [project homepage](https://hugegraph.github.io/hugegraph-doc/) contains more information about `hugegraph-toolchain`. 

## License

hugegraph-toolchain is licensed under `Apache 2.0` License.
