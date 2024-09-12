package com.kenn.book.controller;

import com.github.pagehelper.PageInfo;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.BrowseHistory;
import com.kenn.book.domain.res.TableDataInfo;
import com.kenn.book.service.BrowseHistoryService;
import com.kenn.book.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @ClassName BrowseHistoryController
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月28日 09:51:00
 */
@RestController
@RequestMapping("/browse/history")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(tags = "浏览历史")
public class BrowseHistoryController {

    private final BrowseHistoryService browseHistoryService;

    @PostMapping("/save")
    @ApiOperation("保存历史")
    public Result<?> save(@RequestBody BrowseHistory browseHistory) {
        RLock lock = RedisUtils.getLock(String.format(Constants.BROWSE_HISTORY_LOCK_KEY, browseHistory.getOpenid()));
        boolean flag = false;
        try {
            // 尝试拿锁10s后停止重试,返回false
            // 具有Watch Dog 自动延期机制 默认续30s
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                flag = browseHistoryService.saveHistory(browseHistory);
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
    @ApiOperation("分页获取浏览历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "分页参数,第几页",required = true,type = "query"),
            @ApiImplicitParam(name = "pageSize",value = "分页参数,每页数量",required = true,type = "query"),
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query"),
    })
    public Result<TableDataInfo<BrowseHistory>> list(Integer pageNum, Integer pageSize, String openid) {
        PageInfo<BrowseHistory> pageInfo = browseHistoryService.pageList(pageNum, pageSize, openid);
        return Result.success(TableDataInfo.build(pageInfo));
    }

    @GetMapping("/delete")
    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "书架列表详情id",required = true,type = "query")
    })
    public Result<?> delete(String id) {
        boolean flag = browseHistoryService.delete(id);
        return flag ? Result.success() : Result.error();
    }

}
