package com.kenn.book.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.BrowseHistory;
import com.kenn.book.domain.res.TableDataInfo;
import com.kenn.book.service.BrowseHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
    @Transactional
    public Result<?> save(@RequestBody BrowseHistory browseHistory) {
        QueryWrapper<BrowseHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", browseHistory.getOpenid());
        wrapper.eq("book_name", browseHistory.getBookName());
        wrapper.eq("source", browseHistory.getSource());
        BrowseHistory isExit = browseHistoryService.getOne(wrapper);
        if (ObjectUtil.isNotNull(isExit)) {
            isExit.setCreateTime(new Date());
            browseHistoryService.updateById(isExit);
            return Result.success("更新成功");
        }

        Date operateTime = new Date();
        browseHistory.setCreateTime(operateTime);
        browseHistory.setUpdateTime(operateTime);
        boolean result = browseHistoryService.save(browseHistory);
        return result ? Result.success("保存成功") : Result.error("保存失败");
    }

    @GetMapping("/list")
    @ApiOperation("根据openid获取浏览历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "分页参数,第几页",required = true,type = "query"),
            @ApiImplicitParam(name = "pageSize",value = "分页参数,每页数量",required = true,type = "query"),
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query"),
    })
    public Result<TableDataInfo<BrowseHistory>> list(Integer pageNum, Integer pageSize, String openid) {
        PageHelper.startPage(pageNum, pageSize);
        List<BrowseHistory> result = browseHistoryService.list(new QueryWrapper<BrowseHistory>().eq("openid", openid).orderByDesc("create_time"));
        PageInfo<BrowseHistory> pageInfo = new PageInfo<>(result);
        return Result.success(TableDataInfo.build(pageInfo));
    }

    @GetMapping("/first")
    @ApiOperation("根据openid获取最新一条浏览历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query"),
    })
    public Result<BrowseHistory> first(String openid) {
        BrowseHistory result = browseHistoryService.getOne(new QueryWrapper<BrowseHistory>().eq("openid", openid).orderByDesc("create_time").last("limit 1"));
        return Result.success(result);
    }

    @GetMapping("/delete")
    @ApiOperation("从书架中删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "书架列表详情id",required = true,type = "query")
    })
    public Result<?> delete(String id) {
        BrowseHistory isExist = browseHistoryService.getById(id);
        if (ObjectUtil.isNull(isExist)) {
            return Result.error("已经删除了");
        }
        return Result.success(browseHistoryService.removeById(id));
    }

}
