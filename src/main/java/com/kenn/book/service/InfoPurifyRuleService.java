package com.kenn.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kenn.book.domain.entity.InfoPurifyRule;

import java.util.List;

/**
 * @Description TODO
 * @ClassName InfoPurifyRuleService
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年06月26日 14:19:00
 */
public interface InfoPurifyRuleService extends IService<InfoPurifyRule> {

    List<InfoPurifyRule> listAll();

    boolean importRule(List<InfoPurifyRule> ruleList);

}
