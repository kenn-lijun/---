爱宁呦小说 - 后台服务
小说阅读后台服务, 功能已经实现, 代码和数据库文件全部开源
自定义爬虫源, 爬虫规则文档下面有说明, 存在疑惑可联系作者
目前爬虫源配置接口暂未开发, 只有在数据库中手动配置, 目前库中已配置完成几个源, 可直接使用或参考并创建自己的源
爱宁呦小说 - 小程序
使用uni-app、uView开发
仓库传送门:
需知
因为当初设计的时候是给微信小程序提供后台服务, 同时微信小程序需要配置成https协议, 所以代码中集成了https的相关配置信息, 这可能会导致项目启动失败; 如需https协议支持需自己申请域名和pfx格式证书, 并将证书放置到classpath目录下, 同时在application.yml文件中配置server.ssl.key-store、server.ssl.key-store-type、server.ssl.key-store-password等信息, 如不需要使用https协议, 可注释掉HttpsConfig配置类和相关application.yml中的配置项

爬虫规则
本项目使用Jsoup进行目标html页面解析, 使用Jsoup的css元素选择器获取相关的元素
css选择器语法自行了解或参考: https://www.cnblogs.com/clarke157/p/6432546.html
获取html元素时, 诸如:获取书籍列表、章节列表等相关的html标签元素; 此时规则只有一段, 由纯css元素选择器组成
此时规则表现为: .layout.layout2.layout-co18>ul>li:gt(0)

规则意义: 首先获取class="layout layout2 layout-co18"下的ul标签, 然后获取排在ul标签下第一个li以后的所有li

获取指定内容时, 诸如: 获取封面链接、获取书名、获取章节内容等; 此时规则有两段, 由@符号分隔, 第一段由纯css元素选择器组成, 规则表现同1, 第二段规则是为了获取第一段规则得到的标签下面的文本内容, 支持选项: text、html、href、src、textNodes、元素标签的属性名
此时规则表现为: .top.clearfix>h1>a@href

规则意义: 首先获取class="top clearfix"下的h1标签, 然后获取h1标签下的a标签, 最后获取a标签的href属性值

因为获取章节、下一页、封面、上一章、下一章等相关链接地址时不同的网站规则不一, 难以使用代码统一整理, 所以提供用户自定义js的方式处理输出内容, js代码包含在 <js></js> 标签内, 且需要放置规则的末尾处; 后台代码中内置了两个变量, 第一个是当前页面的baseUrl, 另一个是目标页面的相对地址relativeUrl, 可在js中直接使用
此时规则表现为: li>a@href<js> baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf('/')); return baseUrl + relativeUrl;</js>

规则意义: 首先获取class="top clearfix"下的h1标签, 然后获取h1标签下的a标签, 最后获取a标签的href属性值

js代码: 其中baseUrl是当前页面的url地址(比如: http://www.kenn.com:8080/book/101.html), relativeUrl为当前规则获取的相对地址(比如: /101/10005.html), 最后截取拼接结果为: http://www.kenn.com:8080/book/101/10005.html

免责声明
本项目提供的爬虫源代码仅用学习，请勿用于商业盈利。
用户使用本系统从事任何违法违规的事情，一切后果由用户自行承担作者不承担任何法律责任。
如有侵犯权利，请联系作者删除。
下载本站源码则代表你同意上述的免责声明协议。
无任何商业用途，无任何侵权想法。但如发现侵权或其它问题请 及时 且 成功 与作者本人取得联系。 作者本人会在第一时间进行相关内容的删除。