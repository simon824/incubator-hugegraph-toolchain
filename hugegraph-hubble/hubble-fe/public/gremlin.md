![img.png](/gremlin1.png)  
Gremlin 是 Apache TinkerPop 框架下的图遍历语言。Gremlin 是一种函数式数据流语言，可以使得用户使用简洁的方式表述复杂的属性图（property graph）的遍历或查询。每个Gremlin 遍历由一系列步骤（可能存在嵌套）组成，每一步都在数据流（data stream）上执行一个原子操作。

# 基础查询语法
- 基础概念
  - V()：查询顶点，一般作为图查询的第1步，后面可以续接的语句种类繁多。例，g.V()，g.V('v_id')，查询所有点和特定点；
  - E()：查询边，一般作为图查询的第1步，后面可以续接的语句种类繁多；
  - id()：获取顶点、边的id。例：g.V().id()，查询所有顶点的id；
  - label()：获取顶点、边的 label。例：g.V().label()，可查询所有顶点的label。
  - key() / values()：获取属性的key/value的值。
  - properties()：获取顶点、边的属性；可以和 key()、value()搭配使用，以获取属性的名称或值。例：g.V().properties('name')，查询所有顶点的 
   name 属性；
  - valueMap()：获取顶点、边的属性，以Map的形式体现，和properties()比较像；
  - values()：获取顶点、边的属性值。例，g.V().values() 等于 g.V().properties().value()
- 查询点
```java
// 查询所有点，但限制点的返回数量为100，也可以使用range(x, y)的算子，返回区间内的点数量。
g.V().limit(100)
// 查询点的label值为'software'的点
g.V().hasLabel('software')
// 查询id为‘11’的点
g.V('11')
        
//模糊查询
g.V().has("question_text", textContains('很好'))
//3度查询
g.V().has('name', 'S11:C5>10>>S13').repeat(out()).until(loops().is(3)).path()
g.V('1:Tom Hanks').repeat(both()).times(3).path()
```
- 查询边
```java
// 查询任意100个边
g.E().limit(100)
// 查询边id为‘55-81-5’的边
g.E('55-81-5')
// 查询label为‘develops’的边
g.E().hasLabel('develops')
// 查询点id为‘46’所有label为‘develops’的边
g.V('46').outE('develops')
```
- 查询属性
```java
// 查询点的所有属性（可填参数，表示只查询该点， 一个点所有属性一行结果）
g.V().limit(3).valueMap()
// 查询点的label
g.V().limit(1).label()
// 查询点的name属性（可不填参数，表示查询所有属性， 一个点每个属性一行结果，只有value，没有key）
g.V().limit(10).values('name')
```

# Gremlin 高级语法
## 遍历
顶点为基准：

1. out(label)：根据指定的 Edge Label 来访问顶点的 OUT 方向邻接**点**（可以是零个 Edge Label，代表所有类型边；也可以一个或多个 Edge Label，代表任意给定 Edge Label 的边，下同）；
1. in(label)：根据指定的 Edge Label 来访问顶点的 IN 方向邻接**点**；
1. both(label)：根据指定的 Edge Label 来访问顶点的双向邻接**点**；
1. outE(label)： 根据指定的 Edge Label 来访问顶点的 OUT 方向邻接**边**；
1. inE(label)：根据指定的 Edge Label 来访问顶点的 IN 方向邻接**边**；
1. bothE(label)：根据指定的 Edge Label 来访问顶点的双向邻接**边**；

边为基准的：

1. outV()：访问边的出顶**点**，出顶点是指边的起始顶点；
1. inV()：访问边的入顶**点**，入顶点是指边的目标顶点，也就是箭头指向的顶点；
1. bothV()：访问边的双向顶**点**；
1. otherV()：访问边的伙伴顶**点**，即相对于基准顶点而言的另一端的顶点；
## 过滤
在众多Gremlin的语句中，有一大类是filter类型，顾名思义，就是对输入的对象进行条件判断，只有满足过滤条件的对象才可以通过filter进入下一步。
### has
has语句是filter类型语句的代表，能够以顶点和边的属性作为过滤条件，决定哪些对象可以通过。常用的有下面几种:

1. has(key,value): 通过属性的名字和值来过滤顶点或边；
1. has(label, key, value): 通过label和属性的名字和值过滤顶点和边；
1. has(key,predicate): 通过对指定属性用条件过滤顶点和边，例：g.V().has('age', gt(20))，可得到年龄大于20的顶点；
1. hasLabel(labels…): 通过 label 来过滤顶点或边，满足label列表中一个即可通过；
1. hasId(ids…): 通过 id 来过滤顶点或者边，满足id列表中的一个即可通过；
1. hasKey(keys…): 通过 properties 中的若干 key 过滤顶点或边；
1. hasValue(values…): 通过 properties 中的若干 value 过滤顶点或边；
1. has(key): properties 中存在 key 这个属性则通过，等价于hasKey(key)；
1. hasNot(key): 和 has(key) 相反；

例：

```java
 g.V().hasLabel('person') // lable 等于 person 的所有顶点；
g.V().has('age',inside(20,30)).values('age') // 所有年龄在20（含）~30（不含）之间的顶点；
g.V().has('age',outside(20,30)).values('age') // 所有年龄不在20（含）~30（不含）之间的顶点；
g.V().has('name',within('josh','marko')).valueMap() // name 是'josh'或'marko'的顶点的属性；
g.V().has('name',without('josh','marko')).valueMap() // name 不是'josh'或'marko'的顶点的属性；
g.V().has('name',not(within('josh','marko'))).valueMap() // 同上
g.V().properties().hasKey('age').value() // age 这个属性的所有 value
g.V().hasNot('age').values('name') //  不含 age 这个属性的所有 顶点的 name 属性 
```

## 路径
在使用Gremlin对图进行分析时，关注点有时并不仅仅在最终达到顶点、边或者属性，通过什么样的路径到达最终的顶点、边和属性同样重要。此时可以借助path() 来获取经过的路径信息。
path() 返回当前遍历过的所有路径。有时需要对路径进行过滤，只选择没有环路的路径或者选择包含环路的路径，Gremlin针对这种需求提供了两种过滤路径的step：simplePath() 和cyclicPath()。

1. path()：获取当前遍历过的所有路径；
1. simplePath()：过滤掉路径中含有环路的对象，只保留路径中不含有环路的对象；
1. cyclicPath()：过滤掉路径中不含有环路的对象，只保留路径中含有环路的对象。

例1：寻找4跳以内从 andy 到 jack 的最短路径。

```java
 g.V("andy")
 .repeat(both().simplePath()).until(hasId("target_v_id")
 .and().loops().is(lte(4))).hasId("jack")
 .path().limit(1) 
```

例2：“Titan” 顶点到与其有两层关系的顶点的不含环路的路径（只包含顶点）

```java
g.V()
.hasLabel('software').has('name','Titan')
.both().both().simplePath()
.path() 
```

## 迭代

1. repeat()：指定要重复执行的语句；
1. times()： 指定要重复执行的次数，如执行3次；
   3.until()：指定循环终止的条件，如一直找到某个名字的朋友为止；
1. emit()：指定循环语句的执行过程中收集数据的条件，每一步的结果只要符合条件则被收集，不指定条件时收集所有结果；
   5.loops()：当前循环的次数，可用于控制最大循环次数等，如最多执行3次。

repeat() 和 until() 的位置不同，决定了不同的循环效果：

- repeat() + until()：等同 do-while；
- until() + repeat()：等同 while-do。

repeat() 和 emit() 的位置不同，决定了不同的循环效果：

- repeat() + emit()：先执行后收集；
- emit() + repeat()：表示先收集再执行。

注意：
emit()与times()搭配使用时，是“或”的关系而不是“与”的关系，满足两者间任意一个即可。
emit()与until()搭配使用时，是“或”的关系而不是“与”的关系，满足两者间任意一个即可。
例1：根据出边进行遍历，直到抵达叶子节点（无出边的顶点），输出其路径顶点名：

```java
g.V(1)
 .repeat(out())
 .until(outE().count().is(0))
 .path().by('name') 
```

例2：查询顶点’1’的3度 OUT 可达点路径

```java
 g.V('1')
 .repeat(out())
 .until(loops().is(3))
 .path() 
```

## 转换

1. map()：可以接受一个遍历器 Step 或 Lamda 表达式，将遍历器中的元素映射（转换）成**另一个类型的某个对象**（一对一），以便进行下一步处理
1. flatMap()：可以接受一个遍历器 Step 或 Lamda 表达式，将遍历器中的元素映射（转换）成另一个类型的某个**对象流或迭代器**（一对多）。

稍微了解函数式编程的同学都比较熟悉这两个操作了，在这里不再赘述。
例：

```java
// 创建了 titan 的作者节点的姓名
g.V('titan').in('created').map(values('name'))
// 创建了 titan 的作者节点的所有出边
g.V('titan').in('created').flatMap(outE()) 
```
## 排序

1. order()
1. order().by()

排序比较简单，直接看例子吧。

```java
 // 默认升序排列
g.V().values('name').order()
// 按照元素属性age的值升序排列，并只输出姓名
g.V().hasLabel('person').order().by('age', asc).values('name') 
```
## 逻辑

1. is()：可以接受一个对象（能判断相等）或一个判断语句（如：[P.gt](http://P.gt)()、[P.lt](http://P.lt)()、P.inside()等），当接受的是对象时，原遍历器中的元素**必须与对象相等**才会保留；当接受的是判断语句时，原遍历器中的元素满足判断才会保留，其实接受一个对象相当于P.eq()；
1. and()：可以接受任意数量的遍历器（traversal），原遍历器中的元素，只有在每个新遍历器中**都能生成至少一个输出的情况下**才会保留，相当于过滤器组合的与条件；
1. or()：可以接受任意数量的遍历器（traversal），原遍历器中的元素，只要在全部新遍历器中**能生成至少一个输出**的情况下就会保留，相当于过滤器组合的或条件；
1. not()：仅能接受一个遍历器（traversal），原遍历器中的元素，在新遍历器中能生成输出时会被移除，不能生成输出时则会保留，相当于**过滤器的非条件**。

例1：

```java
// 筛选出顶点属性“age”等于28的属性值，与`is(P.eq(28))`等效
g.V().values('age').is(28)

// 所有包含出边“supports”和“created”的顶点的名字“name”
g.V().
    and(
        outE('supports'), 
        outE('created'))
    .values('name')

// 使用 where语句 实现上面的功能
g.V().where(outE('supports')
            .and()
            .outE('created')
    .values('name')

// 筛选出所有不是“person”的顶点的“label”
g.V().not(hasLabel('person')).label() 
```
例2：获取所有最多只有一条“created”边并且年龄不等于28的“person”顶点

```java
 g.V().hasLabel('person')
 .and(outE('created').count().is(lte(1)), 
      values("age").is(P.not(P.eq(28))))
 .values('name') 
```
## 统计

1. sum()：将 traversal 中的所有的数字求和；
1. max()：对 traversal 中的所有的数字求最大值；
1. min()：对 traversal 中的所有的数字求最小值；
1. mean()：将 traversal 中的所有的数字求均值；
1. count()：统计 traversal 中 item 总数。

例：

```java
// 计算所有“person”的“created”出边数的均值
g.V().hasLabel('person')
    .map(outE('created').count())
    .mean() 
```
## 分支

1. choose() ：分支选择, 常用使用场景为： choose(predicate, true-traversal, false-traversal)，根据 predicate 判断，当前对象满足时，继续 true-traversal，否则继续 false-traversal；
1. optional()：类似于 by() 无实际意义，搭配 choose() 使用；

例1：if-else

```java
 g.V().hasLabel('person').
               choose(values('age').is(lte(30)),
                 __.in(),
                 __.out()).values('name') 
```
例2：if-elseif-else

```java
 // 查找所有的“person”类型的顶点
// 如果“age”属性等于0，输出名字
// 如果“age”属性等于28，输出年龄
// 如果“age”属性等于29，输出他开发的软件的名字
// choose(predicate).option().option()...
g.V().hasLabel('person')
 .choose(values('age'))
 .option(0, values('name'))
 .option(28, values('age'))
 .option(29, __.out('created').values('name'))
 .option(none, values('name')) 
```