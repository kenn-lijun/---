package com.kenn.book.rule;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.entity.BookSearchRule;
import com.kenn.book.domain.entity.ChapterSearchRule;
import com.kenn.book.domain.entity.ExploreSearchRule;
import com.kenn.book.domain.entity.InfoSearchRule;
import com.kenn.book.domain.res.ChapterResult;
import com.kenn.book.domain.res.InfoResult;
import com.kenn.book.domain.res.OkHttpResult;
import com.kenn.book.domain.res.SearchResult;
import com.kenn.book.enums.KennRuleEnum;
import com.kenn.book.exception.BaseException;
import com.kenn.book.service.BookSearchRuleService;
import com.kenn.book.service.ChapterSearchRuleService;
import com.kenn.book.service.ExploreSearchRuleService;
import com.kenn.book.service.InfoSearchRuleService;
import com.kenn.book.utils.*;
import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @Description TODO
 * @ClassName SearchUtils
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年06月21日 13:50:00
 */
@Component
@RequiredArgsConstructor
public class KennSearchUtils {

    private final BookSearchRuleService bookSearchRuleService;
    private final ChapterSearchRuleService chapterSearchRuleService;
    private final InfoSearchRuleService infoSearchRuleService;
    private final ExploreSearchRuleService exploreSearchRuleService;

    public List<SearchResult> searchByBookName(Long sourceId, String name, Integer page) {
        List<SearchResult> bookList = new ArrayList<>();
        try {
            // 获取书籍搜索规则配置
            BookSearchRule bookSearchRule = bookSearchRuleService.getBySourceId(sourceId);
            // 获取用户配置的搜索地址
            String searchUrl = bookSearchRule.getSearchUrl();
            // 用户配置的搜索地址相关参数
            String searchParam = bookSearchRule.getSearchParam();

            String pageCode = RegexUtils.getRegexCode(searchUrl, Constants.PAGE_REGEX_TAG);
            if (StringUtils.isNotEmpty(pageCode)) {
                String pageValue = JsUtils.executePage(String.format(pageCode, page));
                searchUrl = searchUrl.replaceAll(Constants.PAGE_REGEX_TAG, pageValue);
            }

            if (StringUtils.isNotEmpty(searchParam)) {
                pageCode = RegexUtils.getRegexCode(searchParam, Constants.PAGE_REGEX_TAG);
                if (StringUtils.isNotEmpty(pageCode)) {
                    String pageValue = JsUtils.executePage(String.format(pageCode, page));
                    searchParam = searchParam.replaceAll(Constants.PAGE_REGEX_TAG, pageValue);
                }
            }

            // 如果书源配置中不是多页的情况 但是传过来的参数不是第一页就返回空数据
            if (page > 1 && StringUtils.isEmpty(pageCode)) {
                return bookList;
            }

            Map<String, String> paramMap = null;
            if (StringUtils.isNotEmpty(searchParam)) {
                ObjectMapper objectMapper = new ObjectMapper();
                searchParam = String.format(searchParam, name);
                paramMap = objectMapper.readValue(searchParam, new TypeReference<Map<String, String>>() {});
            }

            List<String> executeNodes = RegexUtils.splitJsNode(searchUrl);
            if (CollUtil.isNotEmpty(executeNodes)) {
                StringBuilder sb = new StringBuilder();
                for (String executeNode : executeNodes) {
                    String jsCode = RegexUtils.getRegexCode(executeNode, Constants.JS_REGEX_TAG);
                    if (StringUtils.isNotEmpty(jsCode)) {
                        sb.append(JsUtils.execute(jsCode, name));
                    } else {
                        sb.append(executeNode);
                    }
                }
                searchUrl = sb.toString();
            }

            OkHttpResult okHttpResult = KennSearchUtils.search(searchUrl, paramMap, bookSearchRule.getSearchMethod(), bookSearchRule.getUrlEncoder(), bookSearchRule.getParamCharset(), bookSearchRule.getCharsetName());

            KennAbstractRule abstractRule = SpringUtil.getBean(KennRuleEnum.typeResolveToRule(okHttpResult.getResponseType()));
            abstractRule.addBookList(bookSearchRule, okHttpResult, bookList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public ChapterResult getChapter(Long sourceId, String bookLink) throws Exception {
        // 获取章节列表规则配置
        ChapterSearchRule searchRule = chapterSearchRuleService.getBySourceId(sourceId);
        // 初始化链接
        if (StringUtils.isNotEmpty(searchRule.getInitUrl())) {
            bookLink = JsUtils.execute(RegexUtils.getRegexCode(searchRule.getInitUrl(), Constants.JS_REGEX_TAG), bookLink);
        }
        // 设置ThreadLocal相关变量
        ThreadLocalUtils.setCurrentUrlCache(bookLink);
        OkHttpResult okHttpResult = HttpsUtils.get(bookLink, searchRule.getCharsetName());
        if (!HttpsUtils.judgeResult(okHttpResult)) {
            throw new BaseException("查询失败, 请检查规则地址和参数配置是否正确");
        }
        ChapterResult chapterResult = ChapterResult.builder().build();
        KennAbstractRule abstractRule = SpringUtil.getBean(KennRuleEnum.typeResolveToRule(okHttpResult.getResponseType()));
        abstractRule.addChapterList(searchRule, okHttpResult, chapterResult);
        return chapterResult;
    }

    public InfoResult getInfo(Long sourceId, String chapterLink) throws Exception {
        // 获取阅读页搜索规则配置
        InfoSearchRule searchRule = infoSearchRuleService.getBySourceId(sourceId);

        // 初始化链接
        if (StringUtils.isNotEmpty(searchRule.getInitUrl())) {
            chapterLink = JsUtils.execute(RegexUtils.getRegexCode(searchRule.getInitUrl(), Constants.JS_REGEX_TAG), chapterLink);
        }

        // 设置ThreadLocal相关变量
        ThreadLocalUtils.setCurrentUrlCache(chapterLink);
        OkHttpResult okHttpResult = HttpsUtils.get(chapterLink, searchRule.getCharsetName());
        if (!HttpsUtils.judgeResult(okHttpResult)) {
            throw new BaseException("查询失败, 请检查规则地址和参数配置是否正确");
        }
        InfoResult result = InfoResult.builder().build();
        KennAbstractRule abstractRule = SpringUtil.getBean(KennRuleEnum.typeResolveToRule(okHttpResult.getResponseType()));
        abstractRule.setInfo(searchRule, okHttpResult, result);
        return result;
    }

    public List<SearchResult> explore(Long sourceId, String categoryName, Integer page) throws Exception {
        List<SearchResult> bookList = new ArrayList<>();

        // 获取发现页搜索规则配置
        ExploreSearchRule searchRule = exploreSearchRuleService.getBySourceId(sourceId);

        // 获取发现页分类配置信息
        String categoryInfo = searchRule.getCategoryInfo();
        String searchUrlJsCode = null;
        List<String> executeNodes = RegexUtils.splitJsNode(categoryInfo);
        // size > 1代表有js代码标签
        if (executeNodes.size() > 1) {
            categoryInfo = executeNodes.get(0);
            searchUrlJsCode = RegexUtils.getRegexCode(executeNodes.get(1), Constants.JS_REGEX_TAG);
        }

        List<Map<String, String>> categoryList = new ObjectMapper().readValue(categoryInfo, new TypeReference<List<Map<String, String>>>() {});
        Optional<Map<String, String>> categoryOptional = categoryList.stream().filter(i -> i.get("name").equalsIgnoreCase(categoryName)).findFirst();
        if (!categoryOptional.isPresent()) {
            throw new BaseException("分类信息配置错误");
        }
        // exploreUrl不为空的情况为获取下一页的书籍列表
        String exploreUrl = categoryOptional.get().get("url");

        String pageCode = RegexUtils.getRegexCode(exploreUrl, Constants.PAGE_REGEX_TAG);
        if (StringUtils.isNotEmpty(pageCode)) {
            String pageValue = JsUtils.executePage(String.format(pageCode, page));
            exploreUrl = exploreUrl.replaceAll(Constants.PAGE_REGEX_TAG, pageValue);
        } else {
            // 如果书源配置中不是多页的情况 但是传过来的参数不是第一页就返回空数据
            if (page > 1) {
                return bookList;
            }
        }

        // 设置ThreadLocal相关变量
        ThreadLocalUtils.setCurrentUrlCache(exploreUrl);
        if (StringUtils.isNotEmpty(searchUrlJsCode)) {
            exploreUrl = JsUtils.execute(searchUrlJsCode, exploreUrl);
        }

        OkHttpResult okHttpResult = HttpsUtils.get(exploreUrl, searchRule.getCharsetName());
        if (!HttpsUtils.judgeResult(okHttpResult)) {
            throw new BaseException("查询失败, 请检查规则地址和参数配置是否正确");
        }

        KennAbstractRule abstractRule = SpringUtil.getBean(KennRuleEnum.typeResolveToRule(okHttpResult.getResponseType()));
        BookSearchRule bookSearchRule = BeanUtil.copyProperties(searchRule, BookSearchRule.class);
        // 获取书籍列表
        abstractRule.addBookList(bookSearchRule, okHttpResult, bookList);
        return bookList;
    }

    /**
     * 书源搜索
     * @param url: 接口地址
     * @param paramMap:	接口参数
     * @param searchMethod:	接口类型 1: get 2: post
     * @param urlEncoder: 是否需要url编码
     * @param paramCharset:	参数编码类型
     * @param charset: 接口返回内容编码类型
     * @return: com.kenn.book.domain.res.OkHttpResult
     * @author: kenn
     * @date: 2024/6/21 14:24
     */
    private static OkHttpResult search(String url, Map<String, String> paramMap, Integer searchMethod, Boolean urlEncoder, String paramCharset, String charset) {
        OkHttpResult okHttpResult = null;
        // 1: get 2: post
        if (Integer.valueOf(1).equals(searchMethod)) {
            if (CollUtil.isNotEmpty(paramMap)) {
                okHttpResult = HttpsUtils.get(url + "?" + buildGetParam(paramMap, urlEncoder, paramCharset), charset);
            } else {
                okHttpResult = HttpsUtils.get(url, charset);
            }
        } else if (Integer.valueOf(2).equals(searchMethod)) {
            if (CollUtil.isNotEmpty(paramMap)) {
                okHttpResult = HttpsUtils.postFormParams(url, buildPostForm(paramMap, urlEncoder, paramCharset), charset);
            }
        }
        return okHttpResult;
    }

    private static String buildGetParam(Map<String, String> paramMap, Boolean urlEncoder, String charset) {
        List<String> paramList = new ArrayList<>();
        for (String key : paramMap.keySet()) {
            String value = urlEncoder ? JavaUtils.encodeURI(paramMap.get(key), charset) : paramMap.get(key);
            paramList.add(key + "=" + value);
        }
        return String.join("&", paramList);
    }

    private static FormBody buildPostForm(Map<String, String> paramMap, Boolean isCharset, String charset) {
        FormBody.Builder formBuilder = isCharset ? new FormBody.Builder(Charset.forName(charset)) : new FormBody.Builder();
        for (String key : paramMap.keySet()) {
            formBuilder.add(key, paramMap.get(key));
        }
        return formBuilder.build();
    }

}
