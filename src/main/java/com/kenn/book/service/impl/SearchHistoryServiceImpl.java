package com.kenn.book.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.entity.SearchHistory;
import com.kenn.book.mapper.SearchHistoryMapper;
import com.kenn.book.service.SearchHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @ClassName SearchHistoryServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月26日 16:37:00
 */
@Service
public class SearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory> implements SearchHistoryService {

    @Override
    public List<SearchHistory> historyList(String openid) {
        LambdaQueryWrapper<SearchHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SearchHistory::getInfo);
        queryWrapper.eq(SearchHistory::getOpenid, openid);
        queryWrapper.orderByDesc(SearchHistory::getUpdateTime);
        queryWrapper.last("limit 7");
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistory(SearchHistory searchHistory) {
        Date operateTime = new Date();
        LambdaQueryWrapper<SearchHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SearchHistory::getOpenid, searchHistory.getOpenid());
        queryWrapper.eq(SearchHistory::getInfo, searchHistory.getInfo());
        SearchHistory history = getOne(queryWrapper);
        if (ObjectUtil.isNotNull(history)) {
            history.setUpdateTime(operateTime);
            return updateById(history);
        }
        searchHistory.setCreateTime(operateTime);
        searchHistory.setUpdateTime(operateTime);
        return save(searchHistory);
    }

}
