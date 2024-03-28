package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.entity.MyBookShelf;
import com.kenn.book.mapper.MyBookshelfMapper;
import com.kenn.book.service.MyBookShelfService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @ClassName MyBookShelfServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月21日 14:33:00
 */
@Service
public class MyBookShelfServiceImpl extends ServiceImpl<MyBookshelfMapper, MyBookShelf> implements MyBookShelfService {
}
