package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.entity.SearchHistory;
import com.kenn.book.mapper.SearchHistoryMapper;
import com.kenn.book.service.SearchHistoryService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @ClassName SearchHistoryServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月26日 16:37:00
 */
@Service
public class SearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory> implements SearchHistoryService {
}
