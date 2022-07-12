// Schema 文件在第一次导入或者需要更新schema时指定即可，第二次还指定的话会有异常

/**
 1. 属性（包括点和边的所有属性key）
 - 注意属性值的类型定义，支持：asText/asInt/asDate/asUUID/asBoolean/asByte/asBlob/asDouble/asFloat/asLong
 **/
// company
schema.propertyKey("c_company_uid").asText().ifNotExist().create();
schema.propertyKey("c_company_name").asText().ifNotExist().create();
// school
schema.propertyKey("c_school_uid").asText().ifNotExist().create();
schema.propertyKey("c_school_name").asText().ifNotExist().create();
// talent
schema.propertyKey("c_talent_uid").asText().ifNotExist().create();
schema.propertyKey("c_talent_name").asText().ifNotExist().create();

schema.propertyKey("c_relation_uid").asText().ifNotExist().create();
schema.propertyKey("c_relation_name").asText().ifNotExist().create();

// 学校
schema.propertyKey("创办时间").asText().ifNotExist().create();
schema.propertyKey("外文名").asText().ifNotExist().create();
schema.propertyKey("简称").asText().ifNotExist().create();
schema.propertyKey("办学性质").asText().ifNotExist().create();
schema.propertyKey("校训").asText().ifNotExist().create();
schema.propertyKey("地址").asText().ifNotExist().create();
schema.propertyKey("所属地区").asText().ifNotExist().create();
schema.propertyKey("主管部门").asText().ifNotExist().create();
schema.propertyKey("主要奖项").asText().ifNotExist().create();
schema.propertyKey("现任领导").asText().ifNotExist().create();
schema.propertyKey("学校类别").asText().ifNotExist().create();
schema.propertyKey("学校特色").asText().ifNotExist().create();
schema.propertyKey("院系设置").asText().ifNotExist().create();
schema.propertyKey("院校代码").asText().ifNotExist().create();
schema.propertyKey("现任校长").asText().ifNotExist().create();
schema.propertyKey("知名校友").asText().ifNotExist().create();
schema.propertyKey("类别").asText().ifNotExist().create();
schema.propertyKey("校歌").asText().ifNotExist().create();
schema.propertyKey("本科专业").asText().ifNotExist().create();
schema.propertyKey("类型").asText().ifNotExist().create();
schema.propertyKey("主要院系").asText().ifNotExist().create();
schema.propertyKey("硕士点").asText().ifNotExist().create();
schema.propertyKey("属性").asText().ifNotExist().create();
schema.propertyKey("高职专业").asText().ifNotExist().create();
schema.propertyKey("校庆日").asText().ifNotExist().create();
schema.propertyKey("博士点").asText().ifNotExist().create();
schema.propertyKey("占地面积").asText().ifNotExist().create();
schema.propertyKey("博士后").asText().ifNotExist().create();
schema.propertyKey("国家重点学科").asText().ifNotExist().create();
schema.propertyKey("创办人").asText().ifNotExist().create();
schema.propertyKey("建筑面积").asText().ifNotExist().create();
schema.propertyKey("专职院士数").asText().ifNotExist().create();
schema.propertyKey("学生人数").asText().ifNotExist().create();
schema.propertyKey("知名教师").asText().ifNotExist().create();
schema.propertyKey("院训").asText().ifNotExist().create();
schema.propertyKey("成立时间").asText().ifNotExist().create();
schema.propertyKey("学校官网").asText().ifNotExist().create();
schema.propertyKey("前身").asText().ifNotExist().create();
schema.propertyKey("别名").asText().ifNotExist().create();
schema.propertyKey("所在地").asText().ifNotExist().create();
schema.propertyKey("教职工").asText().ifNotExist().create();
schema.propertyKey("隶属").asText().ifNotExist().create();
schema.propertyKey("所在城市").asText().ifNotExist().create();
schema.propertyKey("特点").asText().ifNotExist().create();
schema.propertyKey("学历").asText().ifNotExist().create();
schema.propertyKey("学校类型").asText().ifNotExist().create();
schema.propertyKey("所在省份").asText().ifNotExist().create();
schema.propertyKey("办学资质").asText().ifNotExist().create();
// 公司
schema.propertyKey("公司介绍").asText().ifNotExist().create();
schema.propertyKey("经营范围").asText().ifNotExist().create();
schema.propertyKey("公司类型").asText().ifNotExist().create();
schema.propertyKey("注册资本").asText().ifNotExist().create();
schema.propertyKey("登记机关").asText().ifNotExist().create();
schema.propertyKey("注册号").asText().ifNotExist().create();
schema.propertyKey("爱企查地址").asText().ifNotExist().create();
schema.propertyKey("知识产权").asText().ifNotExist().create();
schema.propertyKey("类目").asText().ifNotExist().create();
schema.propertyKey("审核/年检日期").asText().ifNotExist().create();
schema.propertyKey("爱企查ID").asText().ifNotExist().create();
schema.propertyKey("营业期限").asText().ifNotExist().create();
schema.propertyKey("成立日期").asText().ifNotExist().create();
schema.propertyKey("经营状态").asText().ifNotExist().create();
schema.propertyKey("统一社会信用代码").asText().ifNotExist().create();
schema.propertyKey("企业地址").asText().ifNotExist().create();
schema.propertyKey("组织机构代码").asText().ifNotExist().create();
schema.propertyKey("法定代表人").asText().ifNotExist().create();
schema.propertyKey("总部地点").asText().ifNotExist().create();
schema.propertyKey("成立时间").asText().ifNotExist().create();
schema.propertyKey("发照时间").asText().ifNotExist().create();
schema.propertyKey("公司名称").asText().ifNotExist().create();
schema.propertyKey("外文名").asText().ifNotExist().create();
schema.propertyKey("公司性质").asText().ifNotExist().create();
schema.propertyKey("公司口号").asText().ifNotExist().create();
schema.propertyKey("员工数").asText().ifNotExist().create();
schema.propertyKey("中文名").asText().ifNotExist().create();
schema.propertyKey("年营业额").asText().ifNotExist().create();
schema.propertyKey("所属行业").asText().ifNotExist().create();
schema.propertyKey("简称").asText().ifNotExist().create();
schema.propertyKey("地址").asText().ifNotExist().create();
schema.propertyKey("创办时间").asText().ifNotExist().create();
schema.propertyKey("主管部门").asText().ifNotExist().create();
schema.propertyKey("类型").asText().ifNotExist().create();
schema.propertyKey("办学性质").asText().ifNotExist().create();
schema.propertyKey("性质").asText().ifNotExist().create();
schema.propertyKey("校训").asText().ifNotExist().create();
schema.propertyKey("创始人").asText().ifNotExist().create();
schema.propertyKey("现任领导").asText().ifNotExist().create();
schema.propertyKey("地理位置").asText().ifNotExist().create();
schema.propertyKey("所属地区").asText().ifNotExist().create();
schema.propertyKey("学校类别").asText().ifNotExist().create();
schema.propertyKey("学校特色").asText().ifNotExist().create();
schema.propertyKey("类别").asText().ifNotExist().create();
schema.propertyKey("别名").asText().ifNotExist().create();
schema.propertyKey("院系设置").asText().ifNotExist().create();
schema.propertyKey("占地面积").asText().ifNotExist().create();
schema.propertyKey("主要奖项").asText().ifNotExist().create();
schema.propertyKey("官网").asText().ifNotExist().create();
schema.propertyKey("董事长").asText().ifNotExist().create();

/**
 2. 点标签
 - 指定主键（主键无法被检索）: `.primaryKeys("c_company_uid")`
 - 指定可以为空的属性: `.nullableKeys("company_uid","company_name")`
 - ID 策略: useCustomizeStringId(自定义String类型【推荐】)、useAutomaticId(自动生成)、usePrimaryKeyId(使用主键)、useCustomizeNumberId(自定义int类型)
 新增属性用.append() 创建点用 .create()
 **/
schema.vertexLabel("company").useCustomizeStringId().properties("c_company_uid","c_company_name","公司介绍","经营范围","公司类型","注册资本","登记机关","注册号","爱企查地址","知识产权","类目","审核/年检日期","爱企查ID","营业期限","成立日期","经营状态","统一社会信用代码","企业地址","组织机构代码","法定代表人","总部地点","成立时间","发照时间","公司名称","外文名","公司性质","公司口号","员工数","中文名","年营业额","所属行业","简称","地址","创办时间","主管部门","类型","办学性质","性质","校训","创始人","现任领导","地理位置","所属地区","学校类别","学校特色","类别","别名","院系设置","占地面积","主要奖项","官网","董事长").nullableKeys("c_company_name","公司介绍","经营范围","公司类型","注册资本","登记机关","注册号","爱企查地址","知识产权","类目","审核/年检日期","爱企查ID","营业期限","成立日期","经营状态","统一社会信用代码","企业地址","组织机构代码","法定代表人","总部地点","成立时间","发照时间","公司名称","外文名","公司性质","公司口号","员工数","中文名","年营业额","所属行业","简称","地址","创办时间","主管部门","类型","办学性质","性质","校训","创始人","现任领导","地理位置","所属地区","学校类别","学校特色","类别","别名","院系设置","占地面积","主要奖项","官网","董事长").create();

schema.vertexLabel("school").useCustomizeStringId().properties("c_school_uid","c_school_name","创办时间","外文名","简称","办学性质","校训","地址","所属地区","主管部门","主要奖项","现任领导","学校类别","学校特色","院系设置","院校代码","现任校长","知名校友","类别","校歌","本科专业","类型","主要院系","硕士点","属性","高职专业","校庆日","博士点","占地面积","博士后","国家重点学科","创办人","建筑面积","专职院士数","学生人数","知名教师","院训","成立时间","学校官网","前身","别名","所在地","教职工","隶属","所在城市","特点","学历","学校类型","所在省份","办学资质").nullableKeys("c_school_name","创办时间","外文名","简称","办学性质","校训","地址","所属地区","主管部门","主要奖项","现任领导","学校类别","学校特色","院系设置","院校代码","现任校长","知名校友","类别","校歌","本科专业","类型","主要院系","硕士点","属性","高职专业","校庆日","博士点","占地面积","博士后","国家重点学科","创办人","建筑面积","专职院士数","学生人数","知名教师","院训","成立时间","学校官网","前身","别名","所在地","教职工","隶属","所在城市","特点","学历","学校类型","所在省份","办学资质").create();

schema.vertexLabel("talent").useCustomizeStringId().properties("c_talent_uid","c_talent_name").ifNotExist().create();

/**
 3. 边标签
 **/
schema.edgeLabel("employ").sourceLabel("talent").targetLabel("company").properties("c_relation_uid","c_relation_name").ifNotExist().create();
schema.edgeLabel("study").sourceLabel("talent").targetLabel("school").properties("c_relation_uid","c_relation_name").ifNotExist().create();


/**
 4. 索引
 - 索引类型：
 - secondary: 支持精确匹配的二级索引，允许建立联合索引，联合索引支持索引前缀搜索
 - range: 支持数值类型的范围查询
 - searcL: 支持全文检索的索引
 - shard: 支持前缀匹配 + 数字范围查询的索引
 - unique: 支持属性值唯一性约束，即可以限定属性的值不重复，允许联合索引，但不支持查询
 **/

schema.indexLabel("companyIndex").onV("company").by("c_company_name").secondary().ifNotExist().create();
schema.indexLabel("schoolIndex").onV("school").by("c_school_name").secondary().ifNotExist().create();
schema.indexLabel("talentIndex").onV("talent").by("c_talent_name").secondary().ifNotExist().create();