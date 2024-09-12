package com.kenn.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenn.book.domain.entity.BookSource;

import java.util.List;

/**
 * @Description TODO
 * @ClassName BookSourceService
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月27日 16:32:00
 */
public interface BookSourceService extends IService<BookSource> {

    void init(Long sourceId) throws JsonProcessingException;

    List<BookSource> listAll();

}
