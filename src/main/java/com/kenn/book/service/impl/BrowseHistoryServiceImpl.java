package com.kenn.book.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kenn.book.domain.entity.BrowseHistory;
import com.kenn.book.mapper.BrowseHistoryMapper;
import com.kenn.book.service.BrowseHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @ClassName BrowseHistoryServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月28日 09:52:00
 */
@Service
public class BrowseHistoryServiceImpl extends ServiceImpl<BrowseHistoryMapper, BrowseHistory> implements BrowseHistoryService {

    @Override
    public List<BrowseHistory> listByParams(String openid) {
        LambdaQueryWrapper<BrowseHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BrowseHistory::getOpenid, openid);
        queryWrapper.orderByDesc(BrowseHistory::getUpdateTime);
        return list(queryWrapper);
    }

    @Override
    public PageInfo<BrowseHistory> pageList(Integer pageNum, Integer pageSize, String openid) {
        PageHelper.startPage(pageNum, pageSize);
        List<BrowseHistory> historyList = listByParams(openid);
        return new PageInfo<>(historyList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistory(BrowseHistory browseHistory) {
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getSourceId, browseHistory.getSourceId());
        wrapper.eq(BrowseHistory::getOpenid, browseHistory.getOpenid());
        wrapper.eq(BrowseHistory::getBookName, browseHistory.getBookName());
        BrowseHistory isExit = getOne(wrapper);
        if (ObjectUtil.isNotNull(isExit)) {
            isExit.setUpdateTime(new Date());
            return updateById(isExit);
        }

        Date operateTime = new Date();
        browseHistory.setCreateTime(operateTime);
        browseHistory.setUpdateTime(operateTime);
        return save(browseHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        return removeById(id);
    }

}
