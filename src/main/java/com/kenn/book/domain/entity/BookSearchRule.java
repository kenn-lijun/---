package com.kenn.book.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description TODO
 * @ClassName BookSearchRule
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 09:52:00
 */
@Data
@TableName("book_search_rule")
@ApiModel(value = "根据书名获取相关书籍规则")
public class BookSearchRule {

    @TableId
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("书源id")
    private Long sourceId;

    @ApiModelProperty("源基础url")
    private String baseUrl;

    @ApiModelProperty("网页编码: 默认utf-8")
    private String charsetName;

    @ApiModelProperty("搜索书籍的url")
    private String searchUrl;

    @ApiModelProperty("请求方式 1: get 2: post")
    private Integer searchMethod;

    @ApiModelProperty("搜索的相关参数")
    private String searchParam;

    @ApiModelProperty("参数是否需要url编码")
    private Boolean urlEncoder;

    @ApiModelProperty("参数编码 默认utf-8")
    private String paramCharset;

    @ApiModelProperty("获取书籍列表的规则")
    private String bookList;

    @ApiModelProperty("获取书名的规则")
    private String bookName;

    @ApiModelProperty("获取书籍链接的规则")
    private String bookUrl;

    @ApiModelProperty("获取作者名称的规则")
    private String author;

    @ApiModelProperty("获取最新章节名称的规则")
    private String lastChapter;

    @ApiModelProperty("获取最新章节链接的规则")
    private String lastChapterUrl;

    @ApiModelProperty("获取更新时间的规则")
    private String updateTime;

    @ApiModelProperty("获取书籍封面链接的规则")
    private String imgUrl;

    @ApiModelProperty("获取下一页的规则")
    private String nextPage;

}
