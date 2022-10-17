


在这种场景里面，通常是对子图的计算或者遍历——这个和左边对全图做计算是完全不一样的：比如说从几个点出发，得到周边3、4度的邻居构成一个子图，再基于这个子图进行计算，根据计算的结果再继续做一些图遍历。所以我们把这种场景称为图数据库。我们现在主要研发内容主要面向OLTP这类场景。

- 
- 
- 环路检测	RingsAPI
- 交点检测	CrosspointsAPI
- 最短路径	ShortestPathAPI
- 全最短路径	AllShortestPathsAPI
- 所有路径	MultiNodeShortestPathAPI
- 模型相似度算法	FusiformSimilarityAPI
- Neighbor Rank推荐算法	NeighborRankAPI
- k步邻居	KneighborAPI
- k跳算法	KoutAPI
- 自定义路径 CustomizedPathsAPI
- 射线检测	RaysAPI
- 共同邻居	SameNeighborsAPI
- 带权最短路径	WeightedShortestPathAPI
- 单源带权最短路径	SingleSourceShortestPathAPI
- Jaccard相似度	JaccardSimilarityAPI
- Personal Rank推荐算法	PersonalRankAPI