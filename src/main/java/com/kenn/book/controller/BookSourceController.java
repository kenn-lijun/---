package com.kenn.book.controller;

import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.BookSource;
import com.kenn.book.service.BookSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description TODO
 * @ClassName BookSourceController
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月27日 16:33:00
 */
@RestController
@RequestMapping("/book/source")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(tags = "书源")
public class BookSourceController {

    private final BookSourceService bookSourceService;

    @GetMapping("/list")
    @ApiOperation("列表")
    public Result<List<BookSource>> list() {
        return Result.success(bookSourceService.listAll());
    }

}
