package com.kenn.book.rule;

import com.kenn.book.domain.Constants;
import com.kenn.book.domain.entity.BookSearchRule;
import com.kenn.book.domain.entity.ChapterSearchRule;
import com.kenn.book.domain.entity.InfoSearchRule;
import com.kenn.book.domain.res.*;
import com.kenn.book.exception.BaseException;
import com.kenn.book.utils.HttpsUtils;
import com.kenn.book.utils.JsUtils;
import com.kenn.book.utils.RegexUtils;
import com.kenn.book.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @ClassName KennAbstractRules
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年06月24日 09:05:00
 */
public interface KennAbstractRule {

    void addBookList(BookSearchRule searchRule, OkHttpResult result, List<SearchResult> bookList);

    default void addChapterList(ChapterSearchRule searchRule, OkHttpResult result, ChapterResult chapter) {
        if (StringUtils.isNotEmpty(searchRule.getInitData())) {
            result.setData(JsUtils.execute(RegexUtils.getRegexCode(searchRule.getInitData(), Constants.JS_REGEX_TAG), result.getData()));
        }
        // 获取封面
        chapter.setImg(parseInfo(searchRule.getImgUrl(), result.getData()));
        // 获取书籍简介
        chapter.setIntroduce(parseInfo(searchRule.getIntro(),  result.getData()));
        // 特殊情况 章节列表不在当前页面 需要点击按钮进行获取
        if (StringUtils.isNotEmpty(searchRule.getChapterPage())) {
            String chapterPageUrl = parseInfo(searchRule.getChapterPage(), result.getData());
            if (StringUtils.isEmpty(chapterPageUrl)) {
                throw new BaseException("章节列表页规则解析错误");
            }
            result = HttpsUtils.get(chapterPageUrl, searchRule.getCharsetName());
        }

        List<ChapterInfoResult> chapterInfoList = new ArrayList<>();
        // 获取章节列表
        if (recursionAddChapter(searchRule, result, chapterInfoList)) {
            // 获取下一页的链接
            String nextPageUrl = parseInfo(searchRule.getNextPage(), result.getData());
            boolean whileFlag = true;
            while (whileFlag && nextPageUrl != null) {
                // 如果存在下一页的情况 循环获取所有章节列表
                result = HttpsUtils.get(nextPageUrl, searchRule.getCharsetName());
                whileFlag = recursionAddChapter(searchRule, result, chapterInfoList);
                nextPageUrl = parseInfo(searchRule.getNextPage(), result.getData());
            }
            chapter.setChapterList(chapterInfoList);
        }
    }

    default void setInfo(InfoSearchRule searchRule, OkHttpResult result, InfoResult infoResult) {
        if (StringUtils.isNotEmpty(searchRule.getInitData())) {
            result.setData(JsUtils.execute(RegexUtils.getRegexCode(searchRule.getInitData(), Constants.JS_REGEX_TAG), result.getData()));
        }
        // 获取小说内容
        String info = parseInfo(searchRule.getInfo(), result.getData());
        // todo 替换p标签为换行符
        if (StringUtils.isNotNull(info)) {
            info = info.replaceAll("<p>", "<br>");
            info = info.replaceAll("</p>", "<br>");
        }
        infoResult.setInfo(info);
        // todo 处理多页的
    }

    boolean recursionAddChapter(ChapterSearchRule searchRule, OkHttpResult result, List<ChapterInfoResult> chapterInfoList);

    String parseInfo(String rule, Object data);

}
