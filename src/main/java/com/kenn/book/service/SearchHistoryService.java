package com.kenn.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kenn.book.domain.entity.SearchHistory;

import java.util.List;

/**
 * @Description TODO
 * @ClassName SearchHistoryService
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月26日 16:37:00
 */
public interface SearchHistoryService extends IService<SearchHistory> {

    List<SearchHistory> historyList(String openid);

    boolean saveHistory(SearchHistory searchHistory);

}
