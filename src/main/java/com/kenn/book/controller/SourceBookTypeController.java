package com.kenn.book.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.BookSource;
import com.kenn.book.domain.entity.ExploreSearchRule;
import com.kenn.book.service.BookSourceService;
import com.kenn.book.service.ExploreSearchRuleService;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @ClassName SourceBookTypeController
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年06月01日 09:00:00
 */
@RestController
@RequestMapping("/source/book/type")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(tags = "书源书籍类别")
public class SourceBookTypeController {

    private final BookSourceService bookSourceService;
    private final ExploreSearchRuleService exploreSearchRuleService;

    @GetMapping("/listBySource")
    @ApiOperation("根据书源获取书籍类别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "书源名称",required = true,type = "query"),
    })
    public Result<List<String>> listBySource(String name) throws Exception {
        BookSource source = bookSourceService.getOne(new QueryWrapper<BookSource>().eq("name", name));
        LambdaQueryWrapper<ExploreSearchRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExploreSearchRule::getSourceId, source.getId());
        ExploreSearchRule searchRule = exploreSearchRuleService.getOne(queryWrapper);
        if (ObjectUtil.isNull(searchRule)) {
            throw new RuntimeException("该书源暂未配置书城规则");
        }
        String categoryInfo = searchRule.getCategoryInfo();
        if (StringUtils.isEmpty(categoryInfo)) {
            throw new RuntimeException("该书源暂未配置书城规则");
        }
        List<Map<String, String>> categoryList = new ObjectMapper().readValue(categoryInfo, new TypeReference<List<Map<String, String>>>() {});
        return Result.success(categoryList.stream().map(i -> i.get("name")).collect(Collectors.toList()));
    }

}
