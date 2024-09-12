package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.entity.InfoPurifyRule;
import com.kenn.book.mapper.InfoPurifyRuleMapper;
import com.kenn.book.service.InfoPurifyRuleService;
import com.kenn.book.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @ClassName InfoPurifyRuleServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年06月26日 14:20:00
 */
@Service
public class InfoPurifyRuleServiceImpl extends ServiceImpl<InfoPurifyRuleMapper, InfoPurifyRule> implements InfoPurifyRuleService {

    @Override
    public List<InfoPurifyRule> listAll() {
        LambdaQueryWrapper<InfoPurifyRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InfoPurifyRule::getIsEnabled, Constants.FLAG_TRUE);
        queryWrapper.orderByAsc(InfoPurifyRule::getSort);
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importRule(List<InfoPurifyRule> ruleList) {
        ruleList.forEach(i->{
            i.setPattern(StringUtils.unescaped(i.getPattern()));
        });
        return saveBatch(ruleList, 1000);
    }

}
