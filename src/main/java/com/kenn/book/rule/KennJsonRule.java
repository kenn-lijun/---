package com.kenn.book.rule;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.jayway.jsonpath.JsonPath;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.entity.BookSearchRule;
import com.kenn.book.domain.entity.ChapterSearchRule;
import com.kenn.book.domain.res.ChapterInfoResult;
import com.kenn.book.domain.res.OkHttpResult;
import com.kenn.book.domain.res.SearchResult;
import com.kenn.book.utils.HttpsUtils;
import com.kenn.book.utils.JsUtils;
import com.kenn.book.utils.StringUtils;
import com.kenn.book.utils.ThreadLocalUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @ClassName KennRuleJsonUtils
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年06月21日 14:38:00
 */
@Component
public final class KennJsonRule implements KennAbstractRule {

    public void addBookList(BookSearchRule searchRule, OkHttpResult result, List<SearchResult> bookList) {
        if (HttpsUtils.judgeResult(result)) {
            String bookListRule = searchRule.getBookList();
            List<String> executeNodeList = JsUtils.splitExecuteNode(bookListRule);

            List<?> resultList = null;
            String data = result.getData();
            for (String executeNode : executeNodeList) {
                if (executeNode.contains(Constants.JS_START_TAG)) {
                    data = JsUtils.execute(JsUtils.getJsCode(executeNode), data);
                } else {
                    resultList = JsonPath.parse(data).read(executeNode, List.class);
                }
            }
            if (resultList != null && resultList.size() > 0) {
                ExecutorService executorService = Executors.newFixedThreadPool(4);
                try {
                    // InheritableThreadLocal 能解决父子线程的值传递，但解决不了线程池里的值传递
                    // TransmittableThreadLocal能解决线程池间的值传递，但解决不了并行流里的值传递
                    List<CompletableFuture<SearchResult>> futures = resultList.stream()
                            .map(bookInfo -> CompletableFuture.supplyAsync(() -> {
                                String bookJson = JSONUtil.toJsonStr(bookInfo);
                                return SearchResult.builder()
                                        .sourceId(searchRule.getSourceId())
                                        .name(parseInfo(searchRule.getBookName(), bookJson)) // 获取书籍名称
                                        .link(parseInfo(searchRule.getBookUrl(), bookJson)) // 获取书籍链接
                                        .author(parseInfo(searchRule.getAuthor(), bookJson)) // 获取作者
                                        .newChapter(parseInfo(searchRule.getLastChapter(), bookJson)) // 获取最新章节名称
                                        .newChapterLink(parseInfo(searchRule.getLastChapterUrl(), bookJson)) // 获取最新章节链接
                                        .updateTime(parseInfo(searchRule.getUpdateTime(), bookJson)) // 获取更新时间
                                        .imgLink(parseInfo(searchRule.getImgUrl(), bookJson)) // 获取书籍封面
                                        .build();
                            }, executorService))
                            .collect(Collectors.toList());

                    List<SearchResult> tempBookList = futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList());

                    bookList.addAll(tempBookList);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    executorService.shutdown();
                    try {
                        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                            executorService.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        executorService.shutdownNow();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    public boolean recursionAddChapter(ChapterSearchRule searchRule, OkHttpResult result, List<ChapterInfoResult> chapterInfoList) {
        boolean flag = false;
        if (HttpsUtils.judgeResult(result)) {
            List<String> executeNodeList = JsUtils.splitExecuteNode(searchRule.getChapterList());

            List<?> chapterList = null;
            String data = result.getData();
            for (String executeNode : executeNodeList) {
                if (executeNode.contains(Constants.JS_START_TAG)) {
                    data = JsUtils.execute(JsUtils.getJsCode(executeNode), data);
                } else {
                    chapterList = JsonPath.parse(data).read(executeNode, List.class);
                }
            }
            if (chapterList != null && chapterList.size() > 0) {
                ExecutorService executorService = Executors.newFixedThreadPool(4);
                try {
                    List<CompletableFuture<ChapterInfoResult>> futures = chapterList.stream()
                            .map(bookInfo -> CompletableFuture.supplyAsync(() -> {
                                String bookJson = JSONUtil.toJsonStr(bookInfo);
                                return ChapterInfoResult.builder()
                                        .name(parseInfo(searchRule.getChapterName(), bookJson)) // 获取章节名称
                                        .link(parseInfo(searchRule.getChapterUrl(), bookJson)) // 获取章节链接
                                        .build();
                            }, executorService))
                            .collect(Collectors.toList());

                    List<ChapterInfoResult> tempChapterList = futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList());

                    chapterInfoList.addAll(tempChapterList);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    executorService.shutdown();
                    try {
                        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                            executorService.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        executorService.shutdownNow();
                        Thread.currentThread().interrupt();
                    }
                }
                flag = true;
            }
        }
        return flag;
    }

    public String parseInfo(String userRule, Object data) {
        String rule = userRule;
        if (StringUtils.isEmpty(rule)) {
            return null;
        }
        String connectorUrl = null;
        if (rule.contains(Constants.CONNECTOR_TAG)) {
            String[] urlSplit = rule.split(Constants.CONNECTOR_TAG);
            String connectorTag = urlSplit[0].trim();
            rule = urlSplit[1].trim();
            if ("baseUrl".equalsIgnoreCase(connectorTag)) {
                connectorUrl = ThreadLocalUtils.getBaseUrl();
            } else if ("currentUrl".equalsIgnoreCase(connectorTag)) {
                connectorUrl = ThreadLocalUtils.getCurrentUrl();
            }
        }
        List<String> executeNodeList = JsUtils.splitExecuteNode(rule);
        String result = ObjectUtil.isNotNull(data) ? data.toString() : null;
        for (String executeNode : executeNodeList) {
            if (executeNode.contains(Constants.JS_START_TAG)) {
                String execute = JsUtils.execute(JsUtils.getJsCode(executeNode), result);
                if (StringUtils.isNotEmpty(execute)) {
                    result = execute;
                }
            } else {
                String infoTag = StringUtils.getInfoTag(executeNode);
                String regex = StringUtils.getRegex(executeNode);
                String replacement = StringUtils.getReplacement(executeNode);
                if (StringUtils.isNotEmpty(infoTag)) {
                    result = JsonPath.parse(result).read(infoTag, String.class);
                }

                if (result != null && regex != null) {
                    result = result.replaceAll(regex, replacement);
                }
            }
        }
        return StringUtils.isEmpty(connectorUrl) ? result : connectorUrl + "," + result;
    }

}
