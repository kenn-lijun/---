package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.entity.BookSource;
import com.kenn.book.mapper.BookSourceMapper;
import com.kenn.book.service.BookSourceService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @ClassName BookSourceServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月27日 16:33:00
 */
@Service
public class BookSourceServiceImpl extends ServiceImpl<BookSourceMapper, BookSource> implements BookSourceService {
}
