package com.kenn.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kenn.book.domain.entity.MyBookShelf;

/**
 * @Description TODO
 * @ClassName MyBookShelfService
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月21日 14:33:00
 */
public interface MyBookShelfService extends IService<MyBookShelf> {

    boolean saveEntity(MyBookShelf myBookShelf);

    boolean delete(Long id);

}
