package com.kenn.book.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.SearchHistory;
import com.kenn.book.service.SearchHistoryService;
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
import java.util.stream.Collectors;

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
    @Transactional(rollbackFor = Exception.class)
    public Result<?> save(@RequestBody SearchHistory searchHistory) {
        LambdaQueryWrapper<SearchHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SearchHistory::getOpenid, searchHistory.getOpenid());
        queryWrapper.eq(SearchHistory::getInfo, searchHistory.getInfo());
        searchHistoryService.remove(queryWrapper);

        Date operateTime = new Date();
        searchHistory.setCreateTime(operateTime);
        searchHistory.setUpdateTime(operateTime);
        boolean result = searchHistoryService.save(searchHistory);
        return result ? Result.success("保存搜索历史成功") : Result.success("保存搜索历史失败");
    }

    @GetMapping("/list")
    @ApiOperation("根据openid获取搜索历史,最多返回七条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query"),
    })
    public Result<List<String>> list(String openid) {
        LambdaQueryWrapper<SearchHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SearchHistory::getInfo);
        queryWrapper.eq(SearchHistory::getOpenid, openid);
        queryWrapper.orderByDesc(SearchHistory::getCreateTime);
        queryWrapper.last("limit 7");
        List<SearchHistory> result = searchHistoryService.list(queryWrapper);
        return Result.success(result.stream().map(SearchHistory::getInfo).collect(Collectors.toList()));
    }

}
