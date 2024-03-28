package com.kenn.book;


import com.kenn.book.controller.SearchController;
import com.kenn.book.domain.*;
import com.kenn.book.utils.JsoupUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookApplicationTests {

    @Autowired
    SearchController searchController;

    @Autowired
    JsoupUtils searchUtils;

    @Test
    public void searchByBookName() throws Exception {
        Result<List<SearchResult>> result = searchController.searchByBookName("斗破苍穹", "笔趣阁5200");
        List<SearchResult> resultList = result.getData();
        for (SearchResult searchResult : resultList) {
            System.out.println(searchResult);
        }
    }

    @Test
    public void chapter() throws Exception {
        Result<ChapterResult> result = searchController.getChapter("http://www.biqu5200.net/0_844/", "笔趣阁5200", null, null, 15);
        ChapterResult chapterResult = result.getData();
        System.out.println(chapterResult.getImg());
        System.out.println(chapterResult.getIntroduce());
        for (ChapterInfo chapterInfo : chapterResult.getChapterList()) {
            System.out.println(chapterInfo);
        }
    }

    @Test
    public void listBySort() throws Exception {
        Result<BookTypeResult> result = searchController.listBySort( "7z小说","玄幻",null);
        if (result.getCode().equals(200)) {
            BookTypeResult data = result.getData();
            for (SearchResult book : data.getBookList()) {
                System.out.println(book);
            }
        }
    }

    @Test
    public void info() throws Exception {
        Result<InfoResult> result = searchController.info( "http://www.biqu5200.net/0_844/638400.html","笔趣阁5200");
        InfoResult infoResult = result.getData();
        System.out.println(infoResult);
    }

    @Test
    public void testSearchBookName() throws Exception {
        List<SearchResult> resultList = searchUtils.searchByBookName(9L, "公司");
        for (SearchResult searchResult : resultList) {
            System.out.println(searchResult);
        }
    }

    @Test
    public void testGetChapter() throws Exception {
        // 3L https://www.bqgbe.com/book/502/
        // 4L http://wap.7zxs.cc/wapbook/8042.html；http://wap.7zxs.cc/wapbook/14046.html；http://wap.7zxs.cc/wapbook/24673.html
        ChapterResult chapter = searchUtils.getChapter(9L, "https://www.sewenwang.top/book/879.html");
        System.out.println(chapter.getIntroduce());
        System.out.println(chapter.getImg());
        for (ChapterInfo chapterInfo : chapter.getChapterList()) {
            System.out.println(chapterInfo);
        }
    }

    @Test
    public void testInfo() throws Exception {
        // 3L https://www.bqgbe.com/book/502/
        // 4L http://wap.7zxs.cc/wapbook/8042.html；http://wap.7zxs.cc/wapbook/14046.html；http://wap.7zxs.cc/wapbook/24673.html
        InfoResult info = searchUtils.getInfo(9L, "https://www.sewenwang.top/book/879/31960.html");
        System.out.println(info);
    }

    @Test
    public void testExplore() throws Exception {
        // 3L https://www.bqgbe.com/book/502/
        // 4L http://wap.7zxs.cc/wapbook/8042.html；http://wap.7zxs.cc/wapbook/14046.html；http://wap.7zxs.cc/wapbook/24673.html
        BookTypeResult explore = searchUtils.explore(9L, "都市", null);
        System.out.println(explore.getNextPageUrl());
        for (SearchResult searchResult : explore.getBookList()) {
            System.out.println(searchResult);
        }
    }

}
