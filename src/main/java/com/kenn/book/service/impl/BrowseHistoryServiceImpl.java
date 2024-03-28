package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.entity.BrowseHistory;
import com.kenn.book.mapper.BrowseHistoryMapper;
import com.kenn.book.service.BrowseHistoryService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @ClassName BrowseHistoryServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月28日 09:52:00
 */
@Service
public class BrowseHistoryServiceImpl extends ServiceImpl<BrowseHistoryMapper, BrowseHistory> implements BrowseHistoryService {
}
