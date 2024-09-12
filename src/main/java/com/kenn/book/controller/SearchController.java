package com.kenn.book.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.BookSource;
import com.kenn.book.domain.res.ChapterResult;
import com.kenn.book.domain.res.InfoResult;
import com.kenn.book.domain.res.SearchResult;
import com.kenn.book.rule.KennSearchUtils;
import com.kenn.book.service.BookSourceService;
import com.kenn.book.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @ClassName SearchController
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年03月12日 09:45:00
 */
@RestController
@RequestMapping("/search")
@Api(tags = "爬虫搜索")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SearchController {

    private final BookSourceService bookSourceService;
    private final Executor threadPoolExecutor;
    private final KennSearchUtils searchUtils;

    @GetMapping("/book/allSource")
    @ApiOperation("根据书名获取书籍列表-全源搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, type = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", required = true, type = "query"),
    })
    public Result<List<SearchResult>> searchBookByAllSource(String name, Integer page) {
        List<BookSource> sourceList = bookSourceService.listAll();
        List<CompletableFuture<List<SearchResult>>> futureList = sourceList.stream()
                .map(source -> CompletableFuture.supplyAsync(() -> searchUtils.searchByBookName(source.getId(), name, page), threadPoolExecutor))
                .collect(Collectors.toList());

        List<SearchResult> resultList = futureList.stream()
                .map(CompletableFuture::join) // 等待所有的future完成
                .filter(CollUtil::isNotEmpty) // 过滤掉空的列表
                .flatMap(List::stream)        // 将多个列表合并成一个流
                .collect(Collectors.toList());
        return Result.success(resultList);
    }

    @GetMapping("/book/singleSource")
    @ApiOperation("根据书名获取书籍列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "书源id", required = true, type = "query"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, type = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", required = true, type = "query"),
    })
    public Result<List<SearchResult>> searchBookBySingleSource(Long sourceId, String name, Integer page) {
        return Result.success(searchUtils.searchByBookName(sourceId, name, page));
    }

    @GetMapping("/getChapter")
    @ApiOperation("根据书籍链接获取章节列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "书源id", required = true, type = "query"),
            @ApiImplicitParam(name = "link", value = "书籍链接", required = true, type = "query"),
    })
    public Result<ChapterResult> getChapter(Long sourceId, String link) throws Exception {
        String chapterCacheKey = "Chapter:" + sourceId + ":" + link;
        ChapterResult searchResult = RedisUtils.getCacheObject(chapterCacheKey);

        if (ObjectUtil.isNull(searchResult)) {
            searchResult = searchUtils.getChapter(sourceId, link);
            searchResult.setTotal(searchResult.getChapterList().size());
            RedisUtils.setCacheObject(chapterCacheKey, searchResult);
            RedisUtils.expire(chapterCacheKey, Duration.ofDays(1));
        }
        return Result.success(searchResult);
    }

    @GetMapping("/info")
    @ApiOperation("根据章节链接获取书籍详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "书源id", required = true, type = "query"),
            @ApiImplicitParam(name = "link", value = "书籍链接", required = true, type = "query"),
    })
    public Result<InfoResult> info(Long sourceId, String link) throws Exception {
        return Result.success(searchUtils.getInfo(sourceId, link));
    }

    @GetMapping("/explore")
    @ApiOperation("根据书城分类标签获取书籍列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "书源id", required = true, type = "query"),
            @ApiImplicitParam(name = "typeName", value = "类别", required = true, type = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", required = true, type = "query"),
    })
    public Result<List<SearchResult>> explore(Long sourceId, String typeName, Integer page) throws Exception {
        return Result.success(searchUtils.explore(sourceId, typeName, page));
    }

    @GetMapping("/cache")
    @ApiOperation("缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "小程序openid", required = true, type = "query"),
            @ApiImplicitParam(name = "key", value = "缓存的key", required = true, type = "query"),
            @ApiImplicitParam(name = "info", value = "缓存的数据", required = true, type = "query"),
    })
    public Result<?> cache(String openid, String key, String info) {
        RedisUtils.setCacheObject(openid + ":" + key, info);
        return Result.success();
    }

    @GetMapping("/getCache")
    @ApiOperation("获取缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "小程序openid", required = true, type = "query"),
            @ApiImplicitParam(name = "key", value = "缓存的key", required = true, type = "query"),
    })
    public Result<?> getCache(String openid, String key) {
        return Result.success(RedisUtils.getCacheObject(openid + ":" + key));
    }

    @GetMapping("/deleteCache")
    @ApiOperation("删除缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "小程序openid", required = true, type = "query"),
            @ApiImplicitParam(name = "key", value = "缓存的key", required = true, type = "query"),
    })
    public Result<?> deleteCache(String openid, String key) {
        RedisUtils.deleteObject(openid + ":" + key);
        return Result.success();
    }

}
