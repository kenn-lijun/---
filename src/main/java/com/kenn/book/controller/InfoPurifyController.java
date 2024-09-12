package com.kenn.book.controller;

import com.kenn.book.domain.Result;
import com.kenn.book.domain.entity.InfoPurifyRule;
import com.kenn.book.service.InfoPurifyRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description TODO
 * @ClassName InfoPurifyController
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年06月26日 16:36:00
 */
@RestController
@RequestMapping("/purify")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(tags = "净化规则")
public class InfoPurifyController {

    private final InfoPurifyRuleService purifyRuleService;

    @PostMapping("/import")
    @ApiOperation("导入")
    public Result<?> importRule(@RequestBody List<InfoPurifyRule> ruleList) {
        boolean flag = purifyRuleService.importRule(ruleList);
        return flag ? Result.success() : Result.error();
    }

}
