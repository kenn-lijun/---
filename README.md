### 爱宁呦小说 - 后台服务
- 小说阅读后台服务, 功能已经实现, 代码和数据库文件全部开源
- 自定义爬虫源, 爬虫规则文档下面有说明, 存在疑惑可联系作者
- 目前爬虫源配置接口暂未开发, 只有在数据库中手动配置, 目前库中已配置完成几个源, 可直接使用或参考并创建自己的源

### 爱宁呦小说 - 小程序
- 使用uni-app、uView开发
- 仓库传送门: [爱宁呦小说 - 小程序](https://gitee.com/jun-kenn/ai-ning-book-ui.git)


### 版本变更项
- 去除服务端集成https相关配置
- 新增安全校验, yml文件中kenn:signKey值自定义, 但是需与小程序requestInterceptors.js中的signKey保持一致
- 优化书源配置规则, 提升查询效率

### 需知
- 微信小程序正式版或体验版需要配置成https协议, 请自行通过nginx代理实现

- 启动项增加-Djasypt.encryptor.password=xxxxx

- yml配置文件里面的重要信息使用ENC(加密字符串) 加密字符串通过下面代码获取

  ```tex
  StringEncryptor encryptor = SpringUtil.getBean(StringEncryptor.class);
  String pass = encryptor.encrypt("123456");
  ```

### 自有标签
- <js>xxx</js>: 

  ```tex
  1. js标签 
  2. 作用: 用于执行js代码串 xxx为js代码串
  3. 支持值: js代码串、page标签、result、baseUrl、currentUrl、自有的java方法(详见JavaUtils和ThreadLocalUtils工具类)
  4. 例:
  result: 后面会有解释 先略过
  baseUrl: 书源基础链接
  currentUrl: 当前请求链接 如获取章节列表时代表的就是书籍链接 获取详情时代表的就是章节链接
  java.ajaxGet(String str) 发起get请求
  ThreadLocalUtils.addHeader(String key, String value) 添加header头
  ```

- <page>xxx</js>: 

  ```tex
  1. page标签 
  2. 作用: 根据当前页码获取值
  3. 支持值: %s、%s组成的数字运算
  4. 例: 
  若用户传过来的页码是2 xxx为%s时得到的就是2 
  若(%s-1)*10时得到的就是10
  ```

- <join>xxx</join>: 
  ```tex
  1. join标签
  2. 作用: 拼接章节链接(获取章节链接时 通过爬虫规则获取到的时相对链接或章节id时 将书籍链接与爬虫结果拼接起来便于后续处理)
  3. 支持值: Jsoup之css选择器爬虫规则、JsonPath爬虫规则
  4. 例:
  书籍链接是http://www.baidu.com/100.html 通过爬虫规则获取到的章节链接是/1001.html 最后返回的结果就是http://www.baidu.com/100.html,/1001.html
  书籍链接是http://www.baidu.com/book/detail?&id=1784900 通过爬虫规则获取到的章节链接是17059214170001  最后返回的结果就是http://www.baidu.com/book/detail?&id=1784900,17059214170001
  ```

### 爬虫规则
- 书源基本信息
  - name: 书源名称(必填)

  - sort: 排序字段(必填)

  - baseUrl: 书源地址(必填)

  - header: 全局请求头配置(必填)(下面规则二选一 不支持同时使用)

    ```tex
    1. 支持json格式数据如: {"Version-Code":"10000","Channel":"mz","appid":"wengqugexs","Version-Name":"1.0.0"}
    2. 支持js标签如: <js>要执行的具体js代码: 通过ThreadLocalUtils.addHeader自行添加请求头</js>
    ```

  - is_delete: 是否删除(必填)(0: 否、1: 是)

- 书籍搜索规则
  - sourceId: 书源id(必填)

  - charsetName: 返回内容的编码规则(必填)(GBK、 UTF-8等)

  - searchUrl: 搜索地址(必填)

    ```tex
    1. 普通字符串链接如: http://www.baidu.com/search.php
    2. 支持js标签如: <js>要执行的具体js代码 需要返回请求的链接</js>
    3. 支持多页占位符<page>%s</page>如: 
    <js>
    
    sign_key='d3dGiJc651gSQ8w1'
    params={'gender':'3','imei_ip':'2937357107','page':<page>%s</page>,'wd':result}
    function urlEncode(param, key, encode) {
       if(param==null) return '';
       var paramStr = '';
       var t = typeof (param);
       if (t == 'string' || t == 'number' || t == 'boolean') {
           paramStr += '&' + key + '=' + ((encode==null||encode) ? encodeURIComponent(param) : param);
       } else {
           for (var i in param) {
               var k = key == null ? i : key + (param instanceof Array ? '[' + i + ']' : '.' + i);
               paramStr += urlEncode(param[i], k, encode);
           }
       }
       return paramStr;
    };
    paramSign=String(java.md5Encode(Object.keys(params).sort().reduce((pre,n)=>pre+n+'='+params[n],'')+sign_key))
    params['sign']=paramSign
    body=urlEncode(params)
    'https://api-bc.wtzw.com/api/v5/search/words?' +body
    
    </js>
    4. 链接+js标签组合如: http://www.baidu.com/search.php<js></js> (注: js标签内部使用result代表的是http://www.baidu.com/search.php)
    ```

  - searchMethod: 请求方式(必填)(1: get、 2: post)    

  - searchParam: 请求参数(非必填)

    ```tex
    支持json格式数据如: {"kw": "%s","pn": "<page>%s</page>","is_author": "0"} 可使用page标签 书名使用%s占位
    ```

  - urlEncoder: 参数是否需要url编码(必填)(1: 是、0: 否)

  - paramCharset: 参数编码规则(非必填)(GBK、 UTF-8等)

  - bookList: 获取书籍列表的爬虫规则(必填)
    ```tex
    1. 支持Jsoup之css选择器爬虫规则 (注: 获取到的是html标签列表)
    2. 支持JsonPath爬虫规则
    3. 支持js标签+JsonPath规则: js标签对书籍进行预处理后的结果再使用JsonPath规则获取(注: js标签的结果必须是json格式数据、js标签内部使用result代表的是通过书籍链接获取的数据 因数据可能加密 需在此解密后使用)
    4. 例: tbody>tr:gt(0) $.data.books[*] <js>xxx</js>$.result[*]
    ```

  - bookName: 获取书籍名称的爬虫规则(必填)
    ```tex
    1. 在bookList获取到的内容基础上进行获取
    2. 支持Jsoup之css选择器爬虫规则 + @ + text/href/src/html (注: text代表爬虫规则获取的html标签文本值 href代表爬虫规则获取的html标签href属性值等)
    3. 支持JsonPath爬虫规则
    4. 例: tr>td:eq(0)>a@text book_data[0].book_name  
    ```

  - bookUrl: 获取书籍链接的爬虫规则(必填)
    ```tex
    1. 在bookList获取到的内容基础上进行获取
    2. 支持Jsoup之css选择器爬虫规则 + @ + text/href/src/href (注: text代表爬虫规则获取的html标签文本值 href代表爬虫规则获取的html标签href属性值等)
    3. 支持JsonPath爬虫规则
    4. 例: tr>td:eq(0)>a@href book_data[0].book_id
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