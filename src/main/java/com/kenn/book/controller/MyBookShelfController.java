package com.kenn.book.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.MyBookShelf;
import com.kenn.book.domain.res.TableDataInfo;
import com.kenn.book.service.MyBookShelfService;
import com.kenn.book.utils.StringUtils;
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
    public Result list(Integer pageNum, Integer pageSize, String openid) {
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
    public Result save(@RequestBody MyBookShelf myBookShelf) {
        if (StringUtils.isEmpty(myBookShelf.getSource())) {
            return Result.error("书源不能为空");
        }
        MyBookShelf isExist = myBookShelfService.getOne(new QueryWrapper<MyBookShelf>().eq("source", myBookShelf.getSource())
                .eq("book_name", myBookShelf.getBookName()).eq("openid", myBookShelf.getOpenid()));
        if (StringUtils.isNotNull(isExist)) {
            return Result.error("已经在书架中");
        }
        return Result.success(myBookShelfService.save(myBookShelf));
    }

    @GetMapping("/delete")
    @ApiOperation("从书架中删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "书架列表详情id",required = true,type = "query")
    })
    public Result delete(String id) {
        MyBookShelf isExist = myBookShelfService.getById(id);
        if (StringUtils.isNull(isExist)) {
            return Result.error("已经删除了");
        }
        return Result.success(myBookShelfService.removeById(id));
    }

    @GetMapping("/isExit")
    @ApiOperation("从书架中删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid",value = "小程序openid",required = true,type = "query"),
            @ApiImplicitParam(name = "bookLink",value = "书籍链接",required = true,type = "query"),
    })
    public Result isExit(String bookLink,String openid) {
        MyBookShelf isExist = myBookShelfService.getOne(new QueryWrapper<MyBookShelf>().eq("openid", openid).eq("book_link", bookLink));
        return Result.success(StringUtils.isNotNull(isExist));
    }

}
