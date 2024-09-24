### 爱宁呦小说 - 后台服务
- 小说阅读后台服务, 功能已经实现, 代码和数据库文件全部开源
- 自定义爬虫源, 爬虫规则文档下面有说明, 存在疑惑可联系作者
- 目前爬虫源配置接口暂未开发, 只有在数据库中手动配置, 目前库中已配置完成几个源, 可直接使用或参考并创建自己的源

### 爱宁呦小说 - 小程序
- 使用uni-app、uView开发
- 仓库传送门: [爱宁呦小说 - 小程序v2.0.0](https://gitee.com/jun-kenn/ai-ning-book-ui/tree/v2.0.0)


### 版本变更项
- 去除服务端集成https相关配置
- 新增接口安全校验
- 优化书源配置规则, 提升查询效率
- 新增jasypt加密配置文件中敏感数据

### 需知
- 微信小程序正式版或体验版需要配置成https协议, 请自行通过nginx代理实现

- 启动项增加-Djasypt.encryptor.password=xxxxx

- yml配置文件里面的重要信息使用ENC(加密字符串) 加密字符串通过下面代码获取

  ```tex
  StringEncryptor encryptor = SpringUtil.getBean(StringEncryptor.class);
  String pass = encryptor.encrypt("123456");
  ```

- yml文件中kenn:signKey定义接口密钥 需与小程序requestInterceptors.js中的signKey保持一致

### 自有标签

- js标签

  > - 用法: `<js>xxx</js>`
  >
  > - 作用: 用于执行js代码串 xxx为js代码串
  >
  > - 支持值: js代码串、page标签、result、baseUrl、currentUrl、自有的java方法(详见JavaUtils和ThreadLocalUtils工具类)
  >
  >   例:
  >
  >   - result: 后面会有解释 先略过
  >   - baseUrl: 书源基础链接
  >   - currentUrl: 当前请求链接 如获取章节列表时代表的就是书籍链接 获取详情时代表的就是章节链接
  >   - java.ajaxGet(String str) 发起get请求
  >   - ThreadLocalUtils.addHeader(String key, String value) 添加header头

- page标签: 

  > - 用法: `<page>xxx</page>`
  >
  > - 作用: 根据当前页码获取值
  >
  > - 支持值: %s、%s组成的数字运算表达式
  >
  >   例: 
  >
  >   - 若用户传过来的页码是2 `<page>%s</page>`得到的就是2 
  >   - 若用户传过来的页码是2 `<page>(%s-1)*10</page>`时得到的就是10

- join标签: 

  > - 用法: `<join>xxx</join>`
  >
  > - 作用: 拼接章节链接(获取章节链接时 通过爬虫规则获取到的时相对链接或章节id时 将书籍链接与爬虫结果拼接起来便于后续处理)
  >
  > - 支持值: Jsoup之css选择器爬虫规则、JsonPath爬虫规则
  >
  >   例:
  >
  >   - 书籍链接是http://www.kenn.com/100.html 通过爬虫规则获取到的章节链接是/1001.html 最后返回的结果就是http://www.kenn.com/100.html,/1001.html
  >   - 书籍链接是http://www.kenn.com/book/detail?&id=1784900 通过爬虫规则获取到的章节链接是17059214170001  最后返回的结果就是http://www.kenn.com/book/detail?&id=1784900,17059214170001

### 爬虫规则
- 书源基本信息(book_source表)
  - name: 书源名称(必填)

  - sort: 排序字段(必填)

  - baseUrl: 书源地址(必填)

  - header: 全局请求头配置(必填)(下面规则二选一 不支持同时使用)

    > - 支持json格式数据
    >
    >   例: `{"Version-Code":"10000","Channel":"mz","appid":"wengqugexs","Version-Name":"1.0.0"}`
    >
    > - 支持js标签
    >
    >   例: `<js>要执行的具体js代码: 通过ThreadLocalUtils.addHeader自行添加请求头</js>`

  - is_delete: 是否删除(必填)(0: 否、1: 是)

- 书籍搜索规则(book_search_rule表)
  - sourceId: 书源id(必填)

  - charsetName: 返回内容的编码规则(必填)(GBK、 UTF-8等)

  - searchUrl: 搜索地址(必填)

    > - 普通字符串链接例: http://www.kenn.com/search.php
    >
    > - 支持js标签
    >
    >   例: `<js>要执行的具体js代码 需要返回请求的链接</js>`
    >
    > - 支持多页占位符`<page>%s</page>`
    >
    >   例: 
    >
    >   `<js>xxx</js>`
    >
    > - 链接+js标签组合
    >   例: `http://www.kenn.com/search.php<js>xxx</js>` (注: js标签内部使用result代表的是http://www.kenn.com/search.php)

  - searchMethod: 请求方式(必填)(1: get、 2: post)    

  - searchParam: 请求参数(非必填)

    > - 支持json格式数据
    >
    >   例: `{"kw": "%s","pn": "<page>%s</page>","is_author": "0"} `
    >
    > - json数据可使用page标签 
    >
    > - json数据书名值使用%s占位

  - urlEncoder: 参数是否需要url编码(必填)(1: 是、0: 否)

  - paramCharset: 参数编码规则(非必填)(GBK、 UTF-8等)

  - bookList: 获取书籍列表的爬虫规则(必填)

    > - 支持Jsoup之css选择器爬虫规则 (注: 获取到的是html标签列表)
    >
    >   例: `tbody>tr:gt(0)`
    >
    > - 支持JsonPath爬虫规则
    >
    >   例: `$.data.books[*]`
    >
    > - 支持js标签+JsonPath规则: js标签对书籍进行预处理后的结果再使用JsonPath规则获取(注: js标签的结果必须是json格式数据、js标签内部使用result代表的是通过书籍链接获取的数据 因数据可能加密 需在此解密后使用)
    >
    >   例: `<js>xxx</js>$.result[*]`

  - bookName: 获取书籍名称的爬虫规则(必填)

    > - 在bookList获取到的内容基础上进行获取
    >
    > - 支持Jsoup之css选择器爬虫规则 + @ + text/href/src/html (注: text代表爬虫规则获取的html标签文本值 href代表爬虫规则获取的html标签href属性值等)
    >
    >   例: `tr>td:eq(0)>a@text`
    >
    > - 支持JsonPath爬虫规则
    >
    >   例: `book_data[0].book_name`

  - bookUrl: 获取书籍链接的爬虫规则(必填)

    > - 在bookList获取到的内容基础上进行获取
    >
    > - 支持Jsoup之css选择器爬虫规则 + @ + text/href/src/href (注: text代表爬虫规则获取的html标签文本值 href代表爬虫规则获取的html标签href属性值等)
    >
    >   例: `tr>td:eq(0)>a@href`
    >
    > - 支持JsonPath爬虫规则
    >
    >   例: `book_data[0].book_id`
  - author: 获取作者的爬虫规则(必填) 同bookName

  - imgUrl: 获取封面链接的爬虫规则(非必填) 同bookName

  - updateTime: 获取更新时间的爬虫规则(非必填) 同bookName

- 章节搜索规则(chapter_search_rule表)

  - sourceId: 书源id(必填)

  - charsetName: 返回内容的编码规则(必填)(GBK、 UTF-8等)

  - initUrl: 初始化搜索链接规则(非必填)

    > 仅支持js标签
    >
    > 例: `<js>要执行的具体js代码 需要返回具体的书籍链接</js>`(注: js标签内部使用result代表的是通过书籍搜索规则获取到的书籍链接)

  - initData: 初始化搜索数据规则(非必填) 

    > 仅支持js标签
    >
    > 例: `<js>要执行的具体js代码 需要返回处理后的数据</js>`(注: 因获取的数据可能是密文 可在此解密后向下传递)
    
  - chapterPage: 章节列表页链接规则(非必填)

    > 因有些特殊情况 如章节信息和章节列表页不在同一个页面 所以需要在这里获取下章节列表页的链接 以下所有的规则返回的结果都应是章节列表页的链接
    > - 支持Jsoup之css选择器爬虫规则+js标签
    >
    >   例: `.button.clearfix>a:first-child@href<js>baseUrl + result</js>`(注: 这里的result代表的js标签前部分爬虫规则获取到的数据)
    >
    > - 支持JsonPath爬虫规则+js标签
    >
    >   例:  `$.result.book_id<js>xxx</js>`
    >
    > - 支持纯js标签
    >
    >   例: `<js>xxx</js>`

  - imgUrl: 获取书籍封面规则(非必填)

    > - 支持Jsoup之css选择器爬虫规则 + @ + text/href/src/html (注: text代表爬虫规则获取的html标签文本值 href代表爬虫规则获取的html标签href属性值等)
    >
    >   例: `#fmimg>img@src`
    >
    > - 支持JsonPath爬虫规则
    >
    >   例: `$.data[0].url`

  - intro: 书籍简介规则(非必填)

    > - 支持Jsoup之css选择器爬虫规则 + @ + text/href/src/html (注: text代表爬虫规则获取的html标签文本值 href代表爬虫规则获取的html标签href属性值等)
    >
    >   例: `#intro@text`
    >
    > - 支持JsonPath爬虫规则
    >
    >   例: `$.data.book.intro`

  - chapterList: 获取章节列表的规则(必填)

    > - 支持Jsoup之css选择器爬虫规则 (注: 获取到的是html标签列表)
    >
    >   例: `.mulu_list>li:nth-of-type(1) ~ li`
    >
    > - 支持JsonPath爬虫规则
    >
    >   例: `$.data.chapter_lists`
    >
    > - JsonPath规则和js标签同时使用(注: js标签内的result代表的是获取到的章节列表的内容)
    >
    >   例: `<js>xxx</js>$.result[*]`

  - chapterName: 获取章节名称的规则(必填)

    > - 在chapterList获取到的内容基础上进行获取
    >
    > - 支持Jsoup之css选择器爬虫规则 + @ + text/href/src/html (注: text代表爬虫规则获取的html标签文本值 href代表爬虫规则获取的html标签href属性值等)
    >
    >   例: `li>a@text`
    >
    > - 支持JsonPath爬虫规则
    >
    >   例: `chapter_name`

  - chapterUrl: 获取章节链接的规则(必填)

    > - 在chapterList获取到的内容基础上进行获取
    >
    > - 支持Jsoup之css选择器爬虫规则 + @ + text/href/src/href (注: text代表爬虫规则获取的html标签文本值 href代表爬虫规则获取的html标签href属性值等)
    >
    >   例: `dd>a@href`
    >
    > - 支持JsonPath爬虫规则
    >
    >   例: `url`
    >
    > - 支持join标签内嵌以上两种规则使用: 用于拼接最终的链接地址 作用参考自由标签章节
    >
    >   例: `<join>dd>a@href</join>、<join>url</join>`

  - nextPage: 获取章节列表页下一页的链接规则(非必填)

    > 因有些特殊情况 章节列表页带分页的情况 所以需要在这里获取下下一页的链接 以至于代码中递归往下查询
    > - 支持Jsoup之css选择器爬虫规则+js标签
    >
    >   例: `.input-group-btn:last-child>a.btn.btn-default:not([disabled])@href<js>baseUrl + result</js>`(注: 这里的result代表的js标签前部分爬虫规则获取到的数据)
    >
    > - 支持JsonPath爬虫规则+js标签
    >
    >   例:  `$.result.book_id<js>xxx</js>`
    >
    > - 带分页的需要遍历查询 效率较低 慎用这种源

- 阅读页搜索规则(info_search_rule表)

  - sourceId: 书源id(必填)

  - charsetName: 返回内容的编码规则(必填)(GBK、 UTF-8等)

  - initUrl: 初始化搜索链接规则(非必填)

    > 仅支持js标签
    >
    > 例: `<js>要执行的具体js代码 需要返回具体的书籍链接</js>`(注: js标签内部使用result代表的是通过章节搜索规则获取到的章节链接)
    
  - initData: 初始化搜索数据规则(非必填) 

    > 仅支持js标签
    >
    > 例: `<js>要执行的具体js代码 需要返回处理后的数据</js>`(注: 因获取的数据可能是密文 可在此解密后向下传递)
    
  - info: 章节内容规则(必填)

    > - 支持Jsoup之css选择器爬虫规则 + @ + text/href/src/html (注: text代表爬虫规则获取的html标签文本值 href代表爬虫规则获取的html标签href属性值等)
    >
    >   例: `#content@html`
    >
    > - 支持JsonPath爬虫规则
    >
    >   例: `$.content`
    >
    > - 支持JsonPath规则+js标签(可能获取到的数据需要解密 js返回具体的章节内容 js内部的result代表JsonPath规则获取到的数据)
    >
    >   例: `$.data.content<js>xxx</js>`
    >
    > - 支持精华规则(注: 只能在最后使用 ##要替换的内容->替换后的内容)
    >
    >   例: `$.content##\n-><br><br>`

- 发现搜索规则(explore_search_rule表)

  - sourceId: 书源id(必填)

  - charsetName: 返回内容的编码规则(必填)(GBK、 UTF-8等)

  - categoryInfo:分类规则(必填)

    > - json格式的分类数据 json内部可使用page标签
    >
    >   例: 
    >
    >   `[{"name":"玄幻","url":"http://www.kenn.com/fenlei/1_<page>%s</page>/"},{"name":"科幻","url":"http://www.kenn.com/fenlei/6_<page>%s</page>/"},{"name":"修真","url":"http://www.kenn.com/fenlei/2_<page>%s</page>/"},{"name":"都市","url":"http://www.kenn.com/fenlei/3_<page>%s</page>/"},{"name":"历史","url":"http://www.kenn.com/fenlei/4_<page>%s</page>/"},{"name":"网游","url":"http://www.kenn.com/fenlei/5_<page>%s</page>/"}]`
    >
    > - 支持json格式数据+js标签(js标签的做一个是对json数据中某一项具体的url做扩展 比如json格式里面的是假链接 需要js处理最后返回真实的链接 js标签里面的result代表json数据中某一项具体的url)
    >
    >   例: 
    >
    >   `[{"name":"都市","url":"https://www.kenn.com/category/gender=2&category_id=203&need_filters=1&page=<page>%s</page>&need_category=1"},{"name":"异能","url":"https://www.kenn.com/category/gender=2&category_id=219&need_filters=1&page=<page>%s</page>&need_category=1"},{"name":"玄幻","url":"https://www.kenn.com/category/gender=2&category_id=202&need_filters=1&page=<page>%s</page>&need_category=1"},{"name":"武侠","url":"https://www.kenn.com/category/gender=2&category_id=205&need_filters=1&page=<page>%s</page>&need_category=1"},{"name":"奇异","url":"https://www.kenn.com/category/gender=2&category_id=204&need_filters=1&page=<page>%s</page>&need_category=1"},{"name":"文学","url":"https://www.kenn.com/category/gender=2&category_id=243&need_filters=1&page=<page>%s</page>&need_category=1"}]<js>xxx</js>`

  - bookList: 获取书籍列表的爬虫规则(必填)

    ```tex
    同书籍搜索规则的bookList
    ```

  - bookName: 获取书籍名称的爬虫规则(必填)

    ```tex
    同书籍搜索规则的bookName
    ```

  - bookUrl: 获取书籍链接的爬虫规则(必填)

    ```tex
    同书籍搜索规则的bookUrl
    ```

  - author: 获取作者的爬虫规则(必填) 同bookName

  - imgUrl: 获取封面链接的爬虫规则(非必填) 同bookName

  - updateTime: 获取更新时间的爬虫规则(非必填) 同bookName

### 免责声明

- 本项目提供的爬虫源代码仅用学习，请勿用于商业盈利。
- 用户使用本系统从事任何违法违规的事情，一切后果由用户自行承担作者不承担任何法律责任。
- 如有侵犯权利，请联系作者删除。
- 下载本站源码则代表你同意上述的免责声明协议。
- 无任何商业用途，无任何侵权想法。但如发现侵权或其它问题请 及时 且 成功 与作者本人取得联系。
  作者本人会在第一时间进行相关内容的删除。