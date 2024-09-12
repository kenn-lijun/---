package com.kenn.book.controller;

import com.kenn.book.domain.Constants;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.SearchHistory;
import com.kenn.book.service.SearchHistoryService;
import com.kenn.book.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @ClassName SearchHistoryController
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月26日 16:38:00
 */
@RestController
@RequestMapping("/search/history")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(tags = "首页搜索历史")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @PostMapping("/save")
    @ApiOperation("保存历史")
    public Result<?> save(@RequestBody SearchHistory searchHistory) {
        RLock lock = RedisUtils.getLock(String.format(Constants.SEARCH_HISTORY_LOCK_KEY, searchHistory.getOpenid()));
        boolean flag = false;
        try {
            // 尝试拿锁10s后停止重试,返回false
            // 具有Watch Dog 自动延期机制 默认续30s
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                flag = searchHistoryService.saveHistory(searchHistory);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return Result.error(exception.getMessage());
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return flag ? Result.success() : Result.error();
    }

    @GetMapping("/list")
    @ApiOperation("根据openid获取搜索历史,最多返回七条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query"),
    })
    public Result<List<SearchHistory>> list(String openid) {
        return Result.success(searchHistoryService.historyList(openid));
    }

}
