package com.kenn.book.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenn.book.domain.*;
import com.kenn.book.domain.entity.BookSearchRule;
import com.kenn.book.domain.entity.ChapterSearchRule;
import com.kenn.book.domain.entity.ExploreSearchRule;
import com.kenn.book.domain.entity.InfoSearchRule;
import com.kenn.book.service.BookSearchRuleService;
import com.kenn.book.service.ChapterSearchRuleService;
import com.kenn.book.service.ExploreSearchRuleService;
import com.kenn.book.service.InfoSearchRuleService;
import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description Jsoup爬虫规则工具: 内置部分自定义规则
 * @ClassName JsoupUtils
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年03月11日 13:47:00
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JsoupUtils {

    private final BookSearchRuleService bookSearchRuleService;
    private final ChapterSearchRuleService chapterSearchRuleService;
    private final InfoSearchRuleService infoSearchRuleService;
    private final ExploreSearchRuleService exploreSearchRuleService;

    public List<SearchResult> searchByBookName(Long sourceId, String name) throws Exception {
        // 获取书籍搜索规则配置
        LambdaQueryWrapper<BookSearchRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookSearchRule::getSourceId, sourceId);
        BookSearchRule bookSearchRule = bookSearchRuleService.getOne(queryWrapper);
        // 获取用户配置的搜索地址
        String searchUrl = bookSearchRule.getSearchUrl();
        // 根据用户配置的搜索地址和相关参数发起请求 获取目标网页document
        Document document = null;
        List<SearchResult> bookList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        // 用户配置的搜索地址相关参数
        String searchParam = String.format(bookSearchRule.getSearchParam(), name);
        HashMap<String, String> paramMap = objectMapper.readValue(searchParam, new TypeReference<HashMap<String, String>>() {});
        // http协议直接使用jsoup爬取网页 https协议使用okhttp3获取网页内容, 然后使用jsoup解析
        if (searchUrl.startsWith("https")) {
            OkHttpResult okHttpResult = null;
            // 1: get 2: post
            if (bookSearchRule.getSearchMethod().equals(1)) {
                if (CollUtil.isNotEmpty(paramMap)) {
                    okHttpResult = HttpsUtils.get(searchUrl + "?" + buildGetParam(paramMap, bookSearchRule.getUrlEncoder(), bookSearchRule.getParamCharset()), bookSearchRule.getCharsetName());
                }
            } else if (bookSearchRule.getSearchMethod().equals(2)) {
                if (CollUtil.isNotEmpty(paramMap)) {
                    okHttpResult = HttpsUtils.postFormParams(searchUrl, buildPostForm(paramMap, bookSearchRule.getUrlEncoder(), bookSearchRule.getParamCharset()), bookSearchRule.getCharsetName());
                }
            }
            if (okHttpResult != null && okHttpResult.getCode().equals(200)) {
                document = Jsoup.parse(okHttpResult.getData());
            }
        } else if (searchUrl.startsWith("http")) {
            HttpURLConnection urlConnection = null;
            if (bookSearchRule.getSearchMethod().equals(1)) {
                URL url = new URL(searchUrl + "?" + buildGetParam(paramMap, bookSearchRule.getUrlEncoder(), bookSearchRule.getParamCharset()));
                //通过调用url.openConnection()来获得一个新的URLConnection对象，并且将其结果强制转换为HttpURLConnection.
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                //设置连接的超时值为30000毫秒，超时将抛出SocketTimeoutException异常
                urlConnection.setConnectTimeout(30000);
                //设置读取的超时值为30000毫秒，超时将抛出SocketTimeoutException异常
                urlConnection.setReadTimeout(30000);
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
            } else if (bookSearchRule.getSearchMethod().equals(2)) {
                URL url = new URL(searchUrl);
                //通过调用url.openConnection()来获得一个新的URLConnection对象，并且将其结果强制转换为HttpURLConnection.
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                //设置连接的超时值为30000毫秒，超时将抛出SocketTimeoutException异常
                urlConnection.setConnectTimeout(30000);
                //设置读取的超时值为30000毫秒，超时将抛出SocketTimeoutException异常
                urlConnection.setReadTimeout(30000);
                //将url连接用于输出，这样才能使用getOutputStream()。getOutputStream()返回的输出流用于传输数据
                urlConnection.setDoOutput(true);
                //设置通用请求属性为默认浏览器编码类型
                urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
                //getOutputStream()返回的输出流，用于写入参数数据
                String content = buildGetParam(paramMap, bookSearchRule.getUrlEncoder(), bookSearchRule.getParamCharset());
                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();
            }
            //此时将调用接口方法。getInputStream()返回的输入流可以读取返回的数据。
            if (urlConnection != null) {
                document = Jsoup.parse(urlConnection.getInputStream(), bookSearchRule.getCharsetName(), bookSearchRule.getBaseUrl());
            }
        }

        if (document != null) {
            // 获取书籍列表
            addBookList(bookSearchRule, document, bookList);
            String nextPageUrl = parseInfo(bookSearchRule.getNextPage(), document, bookSearchRule.getBaseUrl());
            // 如果存在下一页的情况 循环获取所有书籍列表
            while (document != null && nextPageUrl != null) {
                document = getDocument(nextPageUrl, bookSearchRule.getCharsetName());
                addBookList(bookSearchRule, document, bookList);
                nextPageUrl = parseInfo(bookSearchRule.getNextPage(), document, bookSearchRule.getBaseUrl());
            }
        }
        return bookList;
    }

    public ChapterResult getChapter(Long sourceId, String bookLink) throws Exception {
        // 获取章节搜索规则配置
        LambdaQueryWrapper<ChapterSearchRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChapterSearchRule::getSourceId, sourceId);
        ChapterSearchRule searchRule = chapterSearchRuleService.getOne(queryWrapper);

        // 获取章节html页面的document
        Document document = getDocument(bookLink, searchRule.getCharsetName());

        ChapterResult result = ChapterResult.builder().build();
        if (document != null) {
            // 获取封面
            String imgUrl = parseInfo(searchRule.getImgUrl(), document, searchRule.getBaseUrl());
            // 获取书籍简介
            String intro = parseInfo(searchRule.getIntro(), document);
            result.setImg(imgUrl);
            result.setIntroduce(intro);

            // 特殊情况 章节列表不在当前页面 需要点击按钮进行获取
            if (StringUtils.isNotEmpty(searchRule.getChapterPage())) {
                String chapterPageUrl = parseInfo(searchRule.getChapterPage(), document, searchRule.getBaseUrl());
                if (chapterPageUrl == null) {
                    throw new RuntimeException("获取章节列表页面规则解析错误");
                }
                document = getDocument(chapterPageUrl, searchRule.getCharsetName());
            }

            List<ChapterInfo> chapterInfoList = new ArrayList<>();
            // 获取章节列表
            addChapterList(searchRule, bookLink, document, chapterInfoList);

            // 获取下一页的链接
            String nextPageUrl = parseInfo(searchRule.getNextPage(), document, bookLink);
            while (document != null && nextPageUrl != null) {
                // 如果存在下一页的情况 循环获取所有章节列表
                document = getDocument(nextPageUrl, searchRule.getCharsetName());
                addChapterList(searchRule, bookLink, document, chapterInfoList);
                nextPageUrl = parseInfo(searchRule.getNextPage(), document, bookLink);
            }

            result.setChapterList(chapterInfoList);
        }
        return result;
    }

    public InfoResult getInfo(Long sourceId, String bookLink) throws Exception {
        // 获取阅读页搜索规则配置
        LambdaQueryWrapper<InfoSearchRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InfoSearchRule::getSourceId, sourceId);
        InfoSearchRule searchRule = infoSearchRuleService.getOne(queryWrapper);

        // 获取阅读页html页面的document
        Document document = getDocument(bookLink, searchRule.getCharsetName());

        InfoResult result = InfoResult.builder().build();
        if (document != null) {
            // 获取章节名
            String name = parseInfo(searchRule.getName(), document);
            // 获取小说内容
            String info = parseInfo(searchRule.getInfo(), document);
            // 获取下一页链接
            String nextUrl = parseInfo(searchRule.getNextUrl(), document, bookLink);
            // 获取上一页链接
            String beforeUrl = parseInfo(searchRule.getBeforeUrl(), document, bookLink);
            result.setName(name);
            // todo 替换p标签为换行符
            if (StringUtils.isNotNull(info)) {
                info = info.replaceAll("<p>", "<br>");
                info = info.replaceAll("</p>", "<br>");
            }
            result.setInfo(info);
            result.setNextInfoLink(nextUrl);
            result.setBeforeInfoLink(beforeUrl);
            // todo 处理多页的
        }
        return result;
    }

    public BookTypeResult explore(Long sourceId, String categoryName, String exploreUrl) throws Exception {
        // 获取发现页搜索规则配置
        LambdaQueryWrapper<ExploreSearchRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExploreSearchRule::getSourceId, sourceId);
        ExploreSearchRule searchRule = exploreSearchRuleService.getOne(queryWrapper);

        // 获取发现页分类配置信息
        String categoryInfo = searchRule.getCategoryInfo();
        List<Map<String, String>> categoryList = new ObjectMapper().readValue(categoryInfo, new TypeReference<List<Map<String, String>>>() {});
        Optional<Map<String, String>> categoryOptional = categoryList.stream().filter(i -> i.get("name").equalsIgnoreCase(categoryName)).findFirst();
        if (!categoryOptional.isPresent()) {
            throw new RuntimeException("分类信息配置错误");
        }

        // exploreUrl不为空的情况为获取下一页的书籍列表
        if (StringUtils.isEmpty(exploreUrl)) {
            exploreUrl = categoryOptional.get().get("url");
        }
        // 获取发现页html页面的document
        Document document = getDocument(exploreUrl, searchRule.getCharsetName());
        List<SearchResult> bookList = new ArrayList<>();
        BookTypeResult result = BookTypeResult.builder().build();
        if (document != null) {
            BookSearchRule bookSearchRule = BeanUtil.copyProperties(searchRule, BookSearchRule.class);
            // 获取书籍列表
            addBookList(bookSearchRule, document, bookList);
            // 获取下一页链接
            String nextPageUrl = parseInfo(bookSearchRule.getNextPage(), document, bookSearchRule.getBaseUrl());
            result.setBookList(bookList);
            result.setNextPageUrl(nextPageUrl);
        }
        return result;
    }

    private String buildGetParam(HashMap<String, String> paramMap, Boolean urlEncoder, String charset) throws Exception {
        List<String> paramList = new ArrayList<>();
        for (String key : paramMap.keySet()) {
            String value = urlEncoder ? URLEncoder.encode(paramMap.get(key), charset) : paramMap.get(key);
            paramList.add(key + "=" + value);
        }
        return String.join("&", paramList);
    }

    private FormBody buildPostForm(HashMap<String, String> paramMap, Boolean isCharset, String charset) {
        FormBody.Builder formBuilder = isCharset ? new FormBody.Builder(Charset.forName(charset)) : new FormBody.Builder();
        for (String key : paramMap.keySet()) {
            formBuilder.add(key, paramMap.get(key));
        }
        return formBuilder.build();
    }

    private Document getDocument(String link, String charSet) throws Exception {
        // 根据链接获取html页面的document
        Document document = null;
        // http协议直接使用jsoup爬取网页 https协议使用okhttp3获取网页内容, 然后使用jsoup解析
        if (link.startsWith("https")) {
            OkHttpResult okHttpResult = HttpsUtils.get(link, charSet);
            if (okHttpResult != null && okHttpResult.getCode().equals(200)) {
                document = Jsoup.parse(okHttpResult.getData());
            }
        } else if (link.startsWith("http")) {
            URL url = new URL(link);
            //通过调用url.openConnection()来获得一个新的URLConnection对象，并且将其结果强制转换为HttpURLConnection.
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //设置连接的超时值为30000毫秒，超时将抛出SocketTimeoutException异常
            urlConnection.setConnectTimeout(30000);
            //设置读取的超时值为30000毫秒，超时将抛出SocketTimeoutException异常
            urlConnection.setReadTimeout(30000);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
            //此时将调用接口方法。getInputStream()返回的输入流可以读取返回的数据。
            String baseUrl = link.substring(0, link.indexOf("/", 8));
            document = Jsoup.parse(urlConnection.getInputStream(), charSet, baseUrl);
        }
        return document;
    }

    private void addBookList(BookSearchRule searchRule, Element root, List<SearchResult> bookList) {
        if (root != null) {
            Elements bookListElements = root.select(searchRule.getBookList());
            if (CollUtil.isNotEmpty(bookListElements)) {
                for (Element bookListElement : bookListElements) {
                    // 获取书籍名称
                    String bookName = parseInfo(searchRule.getBookName(), bookListElement);
                    // 获取书籍链接
                    String bookUrl = parseInfo(searchRule.getBookUrl(), bookListElement, searchRule.getBaseUrl());
                    // 获取作者
                    String author = parseInfo(searchRule.getAuthor(), bookListElement);
                    // 获取最新章节名称
                    String lastChapter = parseInfo(searchRule.getLastChapter(), bookListElement);
                    // 获取最新章节链接
                    String lastChapterUrl = parseInfo(searchRule.getLastChapterUrl(), bookListElement, searchRule.getBaseUrl());
                    // 获取更新时间
                    String updateTime = parseInfo(searchRule.getUpdateTime(), bookListElement);
                    // 获取书籍封面
                    String imgUrl = parseInfo(searchRule.getImgUrl(), bookListElement, searchRule.getBaseUrl());

                    SearchResult searchResult = SearchResult.builder()
                            .name(bookName)
                            .link(bookUrl)
                            .author(author)
                            .newChapter(lastChapter)
                            .newChapterLink(lastChapterUrl)
                            .updateTime(updateTime)
                            .imgLink(imgUrl)
                            .build();
                    bookList.add(searchResult);
                }
            }
        }
    }

    private void addChapterList(ChapterSearchRule searchRule, String bookUrl, Element root, List<ChapterInfo> chapterInfoList) {
        if (root != null) {
            Elements chapterList = root.select(searchRule.getChapterList());
            if (CollUtil.isNotEmpty(chapterList)) {
                for (Element chapterElement : chapterList) {
                    // 获取章节名称
                    String chapterName = parseInfo(searchRule.getChapterName(), chapterElement);
                    // 获取章节链接
                    String chapterUrl = parseInfo(searchRule.getChapterUrl(), chapterElement, bookUrl);
                    ChapterInfo chapterInfo = ChapterInfo.builder()
                            .name(chapterName)
                            .link(chapterUrl)
                            .build();
                    chapterInfoList.add(chapterInfo);
                }
            }
        }
    }

    /**
     * 根据规则解析html的内容信息
     * @param rule:	用户配置的规则
     * @param root:	html元素
     * @param baseUrl: 基础地址 解析url地址时需要 其他情况不传
     * @return: java.lang.String
     * @author: kenn
     * @date: 2024/3/27 15:18
     */
    private String parseInfo(String rule, Element root, String... baseUrl) {
        if (StringUtils.isEmpty(rule)) {
            return null;
        }
        // 判断规则里是否存在js处理逻辑
        String jsCode = getJsCode(rule);
        if (StringUtils.isNotEmpty(jsCode)) {
            rule = rule.substring(0, rule.indexOf("<js>"));
        }
        String result = null;
        boolean urlFlag = false;
        String[] ruleArr = rule.split("@");
        Element element = null;
        for (int i = 0; i < ruleArr.length - 1; i++) {
            Elements tempElements = root.select(ruleArr[i]);
            if (tempElements.size() > 1) {
                throw new RuntimeException("规则解析异常: expect one, but more than one");
            }
            element = tempElements.first();
        }
        if (element != null) {
            String lastRule = ruleArr[ruleArr.length - 1];
            String tag = lastRule.contains("##") ? lastRule.substring(0, lastRule.lastIndexOf("##")) : lastRule;
            String regex = lastRule.contains("##") ? lastRule.substring(lastRule.lastIndexOf("##") + 2) : null;
            if ("text".equalsIgnoreCase(tag)) {
                result = element.text();
            } else if ("html".equalsIgnoreCase(tag)) {
                result = element.html();
            } else if ("href".equalsIgnoreCase(tag)) {
                urlFlag = true;
                result = element.attr("href");
            } else if ("src".equalsIgnoreCase(tag)) {
                urlFlag = true;
                result = element.attr("src");
            } else if ("textNodes".equalsIgnoreCase(tag)) {
                List<TextNode> textNodes = element.textNodes();
                StringBuilder stringBuilder = new StringBuilder();
                for (TextNode textNode : textNodes) {
                    stringBuilder.append(textNode);
                }
                result = stringBuilder.toString();
            } else {
                try {
                    result = element.attr(tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (result !=null && regex != null) {
                result = result.replaceAll(regex, "");
            }

            if (urlFlag) {
                result = buildUrl(result, baseUrl[0], jsCode);
            }
        }
        return result;
    }

    private static String buildUrl(String relativeUrl, String baseUrl, String jsCode) {
        if (StringUtils.isEmpty(relativeUrl) || relativeUrl.contains("javascript")) {
            return null;
        }
        if (relativeUrl.startsWith("http")) {
            return relativeUrl;
        }
        if (StringUtils.isNotEmpty(jsCode)) {
            return jsAbsoluteUrl(relativeUrl, baseUrl, jsCode);
        }
        String result;
        if (baseUrl.endsWith("/")) {
            if (relativeUrl.startsWith("/")) {
                relativeUrl = relativeUrl.substring(1);
            }
            result = baseUrl + relativeUrl;
        } else {
            result = relativeUrl.startsWith("/") ? baseUrl + relativeUrl : baseUrl + "/" + relativeUrl;
        }
        return result;
    }

    /**
     * 通过用户自定义js代码获取目标地址
     * @param relativeUrl: 相对地址
     * @param baseUrl: 基础地址
     * @param customCode: 用户自定义js代码
     * @return: java.lang.String
     * @author: kenn
     * @date: 2024/3/27 13:51
     */
    private static String jsAbsoluteUrl(String relativeUrl, String baseUrl, String customCode) {
        String result = null;
        // 创建一个ScriptEngineManager对象
        ScriptEngineManager manager = new ScriptEngineManager();
        // 获取JavaScript引擎
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        String templateCode = "function getAbsoluteUrl() {" +
                    "   var relativeUrl = '%s';" +
                    "   var baseUrl = '%s';" +
                    "%s" +
                "}";

        String functionCode = String.format(templateCode, relativeUrl, baseUrl, customCode);
        try {
            // 执行js代码片段
            engine.eval(functionCode);
            // 获取Invocable接口并调用JavaScript函数
            Invocable invocable = (Invocable) engine;
            Object executeScript = invocable.invokeFunction("getAbsoluteUrl");
            result = executeScript.toString();
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取用户配置的js代码串 代码串一般用<js></js>包含 目前只支持规则末尾配置一个js代码串
     * @param ruleStr: 用户配置的规则信息
     * @return: java.lang.String
     * @author: kenn
     * @date: 2024/3/27 15:16
     */
    public static String getJsCode(String ruleStr) {
        String result = null;
        Pattern pattern = Pattern.compile("<js>(.*?)</js>");
        Matcher matcher = pattern.matcher(ruleStr);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

}
