package com.kenn.book.rule;

import cn.hutool.core.util.ObjectUtil;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.entity.BookSearchRule;
import com.kenn.book.domain.entity.ChapterSearchRule;
import com.kenn.book.domain.res.ChapterInfoResult;
import com.kenn.book.domain.res.OkHttpResult;
import com.kenn.book.domain.res.SearchResult;
import com.kenn.book.utils.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
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
public class KennHtmlRule implements KennAbstractRule {

    public void addBookList(BookSearchRule searchRule, OkHttpResult result, List<SearchResult> bookList) {
        if (HttpsUtils.judgeResult(result)) {
            Document document = Jsoup.parse(result.getData());
            Elements bookListElements = document.select(searchRule.getBookList());
            if (!bookListElements.isEmpty()) {
                // InheritableThreadLocal 能解决父子线程的值传递，但解决不了线程池里的值传递
                // TransmittableThreadLocal能解决线程池间的值传递，但解决不了并行流里的值传递
                ExecutorService executorService = Executors.newFixedThreadPool(4);
                try {
                    List<CompletableFuture<SearchResult>> futures = bookListElements.stream()
                            .map(element -> CompletableFuture.supplyAsync(() -> {
                                String name = parseInfo(searchRule.getBookName(), element);
                                if (StringUtils.isNotEmpty(name)) {
                                    return SearchResult.builder()
                                            .sourceId(searchRule.getSourceId())
                                            .name(name)
                                            .link(parseInfo(searchRule.getBookUrl(), element))
                                            .author(parseInfo(searchRule.getAuthor(), element))
                                            .newChapter(parseInfo(searchRule.getLastChapter(), element))
                                            .newChapterLink(parseInfo(searchRule.getLastChapterUrl(), element))
                                            .updateTime(parseInfo(searchRule.getUpdateTime(), element))
                                            .imgLink(parseInfo(searchRule.getImgUrl(), element))
                                            .build();
                                }
                                return null;
                            }, executorService))
                            .collect(Collectors.toList());

                    List<SearchResult> tempBookList = futures.stream()
                            .map(CompletableFuture::join)
                            .filter(Objects::nonNull)
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
            Document root = Jsoup.parse(result.getData());
            Elements chapterList = root.select(searchRule.getChapterList());
            if (!chapterList.isEmpty()) {
                ExecutorService executorService = Executors.newFixedThreadPool(4);
                try {
                    List<CompletableFuture<ChapterInfoResult>> futures = chapterList.stream()
                            .map(element -> CompletableFuture.supplyAsync(() -> {
                                // 获取章节名称
                                String chapterName = parseInfo(searchRule.getChapterName(), element);
                                if (StringUtils.isNotEmpty(chapterName)) {
                                    return ChapterInfoResult.builder()
                                            .name(parseInfo(searchRule.getChapterName(), element)) // 获取章节名称
                                            .link(parseInfo(searchRule.getChapterUrl(), element)) // 获取章节链接
                                            .build();
                                }
                                return null;
                            }, executorService))
                            .collect(Collectors.toList());

                    List<ChapterInfoResult> tempChapterList = futures.stream()
                            .map(CompletableFuture::join)
                            .filter(Objects::nonNull)
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


    /**
     * 根据规则解析html的内容信息
     * @param rule:	用户配置的规则
     * @param root:	html元素
     * @return: java.lang.String
     * @author: kenn
     * @date: 2024/3/27 15:18
     */
    public String parseInfo(String rule, Object root) {
        if (StringUtils.isEmpty(rule) || ObjectUtil.isNull(root)) {
            return null;
        }
        String joinCode = RegexUtils.getRegexCode(rule, Constants.JOIN_REGEX_TAG);
        if (StringUtils.isNotEmpty(joinCode)) {
            rule = joinCode;
        }
        List<String> executeNodeList = RegexUtils.splitJsNode(rule);
        String result = null;
        for (String executeNode : executeNodeList) {
            String jsCode = RegexUtils.getRegexCode(executeNode, Constants.JS_REGEX_TAG);
            if (StringUtils.isNotEmpty(jsCode)) {
                String execute = JsUtils.execute(jsCode, result);
                if (StringUtils.isNotEmpty(execute)) {
                    result = execute;
                }
            } else {
                String[] ruleArr = executeNode.split("@");
                Element element = root instanceof Element ? (Element) root : Jsoup.parse(root.toString());
                for (int i = 0; i < ruleArr.length - 1; i++) {
                    if (element != null) {
                        Elements tempElements = element.select(ruleArr[i]);
                        if (tempElements.size() == 1) {
                            element = tempElements.first();
                        }
                    }
                }
                if (element != null) {
                    String lastRule = ruleArr[ruleArr.length - 1];
                    String infoTag = StringUtils.getInfoTag(lastRule);
                    String regex = StringUtils.getRegex(lastRule);
                    String replacement = StringUtils.getReplacement(lastRule);
                    if ("text".equalsIgnoreCase(infoTag)) {
                        result = element.text();
                    } else if ("html".equalsIgnoreCase(infoTag)) {
                        result = element.html();
                    } else if ("href".equalsIgnoreCase(infoTag)) {
                        result = element.attr("href");
                    } else if ("src".equalsIgnoreCase(infoTag)) {
                        result = element.attr("src");
                    } else if ("textNodes".equalsIgnoreCase(infoTag)) {
                        List<TextNode> textNodes = element.textNodes();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (TextNode textNode : textNodes) {
                            stringBuilder.append(textNode);
                        }
                        result = stringBuilder.toString();
                    } else if (StringUtils.isNotEmpty(infoTag)) {
                        try {
                            result = element.attr(infoTag);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (result != null && regex != null) {
                        result = result.replaceAll(regex, replacement);
                    }
                }
            }
        }
        return StringUtils.isNotEmpty(joinCode) ? ThreadLocalUtils.getCurrentUrl() + "," + result : result;
    }

}
