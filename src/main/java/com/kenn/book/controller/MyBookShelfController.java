package com.kenn.book.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.MyBookShelf;
import com.kenn.book.domain.res.TableDataInfo;
import com.kenn.book.service.MyBookShelfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description TODO
 * @ClassName MyBookShelfController
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月21日 14:31:00
 */
@RestController
@RequestMapping("/bookshelf")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(tags = "我的书架")
public class MyBookShelfController {

    private final MyBookShelfService myBookShelfService;

    @GetMapping("/list")
    @ApiOperation("书架列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "分页参数,第几页",type = "query"),
            @ApiImplicitParam(name = "pageSize",value = "分页参数,每页数量",type = "query"),
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query")
    })
    public Result<?> list(Integer pageNum, Integer pageSize, String openid) {
        if (ObjectUtil.isNotNull(pageNum) && ObjectUtil.isNotNull(pageSize)) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<MyBookShelf> list = myBookShelfService.list(new QueryWrapper<MyBookShelf>().eq("openid", openid));

        if (ObjectUtil.isNotNull(pageNum) && ObjectUtil.isNotNull(pageSize)) {
            PageInfo<MyBookShelf> pageInfo = new PageInfo<>(list);
            return Result.success(TableDataInfo.build(pageInfo));
        } else {
            return Result.success(list);
        }
    }

    @PostMapping("/save")
    @ApiOperation("添加到书架")
    public Result<?> save(@RequestBody MyBookShelf myBookShelf) {
        boolean flag = myBookShelfService.saveEntity(myBookShelf);
        return flag ? Result.success() : Result.error();
    }

    @GetMapping("/delete")
    @ApiOperation("从书架中删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "书架列表详情id",required = true,type = "query")
    })
    public Result<?> delete(Long id) {
        boolean flag = myBookShelfService.delete(id);
        return flag ? Result.success() : Result.error();
    }

    @GetMapping("/isExit")
    @ApiOperation("是否已经在书架中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId",value = "书源id",required = true,type = "query"),
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query"),
            @ApiImplicitParam(name = "bookName",value = "书籍名称",required = true,type = "query"),
    })
    public Result<?> isExit(Long sourceId, String openid, String bookName) {
        LambdaQueryWrapper<MyBookShelf> exitWrapper = new LambdaQueryWrapper<>();
        exitWrapper.eq(MyBookShelf::getSourceId, sourceId);
        exitWrapper.eq(MyBookShelf::getOpenid, openid);
        exitWrapper.eq(MyBookShelf::getBookName, bookName);
        return Result.success(myBookShelfService.count(exitWrapper) > 0);
    }

}
