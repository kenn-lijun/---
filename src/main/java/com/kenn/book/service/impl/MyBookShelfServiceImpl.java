package com.kenn.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenn.book.domain.entity.MyBookShelf;
import com.kenn.book.exception.BaseException;
import com.kenn.book.mapper.MyBookshelfMapper;
import com.kenn.book.service.MyBookShelfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @Description TODO
 * @ClassName MyBookShelfServiceImpl
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月21日 14:33:00
 */
@Service
public class MyBookShelfServiceImpl extends ServiceImpl<MyBookshelfMapper, MyBookShelf> implements MyBookShelfService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveEntity(MyBookShelf myBookShelf) {
        Assert.notNull(myBookShelf.getSourceId(),"书源不能为空");
        LambdaQueryWrapper<MyBookShelf> exitWrapper = new LambdaQueryWrapper<>();
        exitWrapper.eq(MyBookShelf::getSourceId, myBookShelf.getSourceId());
        exitWrapper.eq(MyBookShelf::getOpenid, myBookShelf.getOpenid());
        exitWrapper.eq(MyBookShelf::getBookName, myBookShelf.getBookName());
        if (count(exitWrapper) > 0) {
            throw new BaseException("已经在书架中");
        }
        return save(myBookShelf);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

}
