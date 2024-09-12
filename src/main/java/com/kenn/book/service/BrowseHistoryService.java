package com.kenn.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.kenn.book.domain.entity.BrowseHistory;

import java.util.List;

/**
 * @Description TODO
 * @ClassName BrowseHistoryService
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月28日 09:52:00
 */
public interface BrowseHistoryService extends IService<BrowseHistory> {

    List<BrowseHistory> listByParams(String openid);

    PageInfo<BrowseHistory> pageList(Integer pageNum, Integer pageSize, String openid);

    boolean saveHistory(BrowseHistory browseHistory);

    boolean delete(String id);

}
