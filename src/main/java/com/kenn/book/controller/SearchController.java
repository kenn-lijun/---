package com.kenn.book.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kenn.book.domain.*;
import com.kenn.book.domain.entity.BookSource;
import com.kenn.book.service.BookSourceService;
import com.kenn.book.utils.CollectionUtils;
import com.kenn.book.utils.JsoupUtils;
import com.kenn.book.utils.RedisCache;
import com.kenn.book.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
    private final RedisCache redisCache;
    private final AsyncSaveInfo asyncSaveInfo;
    private final JsoupUtils jsoupUtils;

    @GetMapping("/searchByBookName")
    @ApiOperation("根据书名获取书籍列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, type = "query"),
            @ApiImplicitParam(name = "source", value = "书源", required = true, type = "query"),
    })
    public Result<List<SearchResult>> searchByBookName(String name, String source) throws Exception {
        BookSource bookSource = bookSourceService.getOne(new QueryWrapper<BookSource>().eq("name", source));
        return Result.success(jsoupUtils.searchByBookName(bookSource.getId(), name));
    }

    @GetMapping("/getChapter")
    @ApiOperation("根据书籍链接获取章节列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "link", value = "书籍链接", required = true, type = "query"),
            @ApiImplicitParam(name = "source", value = "书源", required = true, type = "query"),
            @ApiImplicitParam(name = "nowLink", value = "当前章节", type = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", type = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", type = "query"),
    })
    public Result<ChapterResult> getChapter(String link, String source, String nowLink, Integer pageNum, Integer pageSize) throws Exception {
        BookSource bookSource = bookSourceService.getOne(new QueryWrapper<BookSource>().eq("name", source));

        String chapterCacheKey = "Chapter:" + source + ":" + link;
        ChapterResult searchResult = redisCache.getCacheObject(chapterCacheKey);

        if (ObjectUtil.isNull(searchResult)) {
            searchResult = jsoupUtils.getChapter(bookSource.getId(), link);
            List<ChapterInfo> tempChapterList = searchResult.getChapterList();
            searchResult.setTotal(tempChapterList.size());
            String chapterFirstLinkKey = "Book:" + source + ":" + link;
            redisCache.setCacheObject(chapterFirstLinkKey, tempChapterList.get(0).getLink());
            redisCache.setCacheObject(chapterCacheKey, searchResult, 1, TimeUnit.HOURS);
        }

        List<ChapterInfo> chapterList = searchResult.getChapterList();
        if (chapterList != null && chapterList.size() > 3000) {
            if (ObjectUtil.isNotNull(pageNum) && ObjectUtil.isNotNull(pageSize)) {
                List<List<ChapterInfo>> groupList = CollectionUtils.groupOnPieceSize(chapterList, pageSize);
                searchResult.setChapterList(groupList.get(pageNum - 1));
                searchResult.setCurrentPage(pageNum);
            } else if (StringUtils.isEmpty(nowLink)) {
                searchResult.setChapterList(chapterList.stream().limit(pageSize).collect(Collectors.toList()));
                searchResult.setCurrentPage(1);
            } else {
                List<List<ChapterInfo>> groupList = CollectionUtils.groupOnPieceSize(chapterList, pageSize);
                for (int i = 0; i < groupList.size(); i++) {
                    Optional<ChapterInfo> optional = groupList.get(i).stream().filter(j -> nowLink.equalsIgnoreCase(j.getLink())).findFirst();
                    if (optional.isPresent()) {
                        searchResult.setChapterList(groupList.get(i));
                        searchResult.setCurrentPage(i + 1);
                        break;
                    }
                }
            }
        }
        return Result.success(searchResult);
    }

    @GetMapping("/getFirstLinkByCache")
    @ApiOperation("根据书籍链接获取第一章链接-从缓存获取")
    public Result<?> getFirstLinkByCache(String source, String link) {
        String key = "Book:" + source + ":" + link;
        String result = redisCache.getCacheObject(key);
        return Result.success(result);
    }

    @GetMapping("/info")
    @ApiOperation("根据章节链接获取书籍详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "link", value = "书籍链接", required = true, type = "query"),
            @ApiImplicitParam(name = "source", value = "书源", required = true, type = "query"),
    })
    public Result<InfoResult> info(String link, String source) throws Exception {
        BookSource bookSource = bookSourceService.getOne(new QueryWrapper<BookSource>().eq("name", source));
        String infoCacheKey = "Info:" + source + ":" + link;
        InfoResult infoResult = redisCache.getCacheObject(infoCacheKey);

        if (ObjectUtil.isNull(infoResult) || StringUtils.isEmpty(infoResult.getInfo())) {
            infoResult =jsoupUtils.getInfo(bookSource.getId(),link);
            redisCache.setCacheObject(infoCacheKey, infoResult);
        }

        asyncSaveInfo.saveInfo(infoResult, bookSource);
        return Result.success(infoResult);
    }

    @GetMapping("/listBySort")
    @ApiOperation("根据书源分类获取书籍列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "source", value = "书源", required = true, type = "query"),
            @ApiImplicitParam(name = "typeName", value = "类别", required = true, type = "query"),
            @ApiImplicitParam(name = "nextPageUrl", value = "下一页地址", required = true, type = "query"),
    })
    public Result<BookTypeResult> listBySort(String source, String typeName, String nextPageUrl) throws Exception {
        BookSource bookSource = bookSourceService.getOne(new QueryWrapper<BookSource>().eq("name", source));
        BookTypeResult bookTypeResult = jsoupUtils.explore(bookSource.getId(), typeName, nextPageUrl);
        return Result.success(bookTypeResult);
    }

    @GetMapping("/cache")
    @ApiOperation("缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "小程序openid", required = true, type = "query"),
            @ApiImplicitParam(name = "key", value = "缓存的key", required = true, type = "query"),
            @ApiImplicitParam(name = "info", value = "缓存的数据", required = true, type = "query"),
    })
    public Result<?> cache(String openid, String key, String info) {
        redisCache.setCacheObject(openid + ":" + key, info);
        return Result.success();
    }

    @GetMapping("/getCache")
    @ApiOperation("获取缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "小程序openid", required = true, type = "query"),
            @ApiImplicitParam(name = "key", value = "缓存的key", required = true, type = "query"),
    })
    public Result<?> getCache(String openid, String key) {
        String info = redisCache.getCacheObject(openid + ":" + key);
        return Result.success(StringUtils.isEmpty(info) ? "" : info);
    }

    @GetMapping("/deleteCache")
    @ApiOperation("删除缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "小程序openid", required = true, type = "query"),
            @ApiImplicitParam(name = "key", value = "缓存的key", required = true, type = "query"),
    })
    public Result<?> deleteCache(String openid, String key) {
        redisCache.deleteObject(openid + ":" + key);
        return Result.success();
    }

}
