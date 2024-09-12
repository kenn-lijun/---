package com.kenn.book.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.ExploreSearchRule;
import com.kenn.book.exception.BaseException;
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
@RequestMapping("/explore")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(tags = "书城")
public class ExploreController {

    private final ExploreSearchRuleService exploreSearchRuleService;

    @GetMapping("/tag/list")
    @ApiOperation("根据书源获取书城分组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId",value = "书源id",required = true,type = "query"),
    })
    public Result<List<String>> tagList(Long sourceId) throws Exception {
        ExploreSearchRule searchRule = exploreSearchRuleService.getBySourceId(sourceId);

        String categoryInfo = searchRule.getCategoryInfo();
        if (StringUtils.isEmpty(categoryInfo)) {
            throw new BaseException("该书源暂未配置书城规则");
        }
        List<Map<String, String>> categoryList = new ObjectMapper().readValue(categoryInfo, new TypeReference<List<Map<String, String>>>() {});
        return Result.success(categoryList.stream().map(i -> i.get("name")).collect(Collectors.toList()));
    }

}
