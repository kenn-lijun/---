package com.kenn.book.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description TODO
 * @ClassName ChapterSearchRule
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:00:00
 */
@Data
@TableName("chapter_search_rule")
@ApiModel(value = "根据书籍链接获取相关书籍规则")
public class ChapterSearchRule {

    @TableId
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("书源id")
    private Long sourceId;

    @ApiModelProperty("初始化链接js")
    private String initUrl;

    @ApiModelProperty("初始化数据js")
    private String initData;

    @ApiModelProperty("章节列表页 特殊情况：列表需要重新获取")
    private String chapterPage;

    @ApiModelProperty("获取书籍封面链接的规则")
    private String imgUrl;

    @ApiModelProperty("获取介绍的规则")
    private String intro;

    @ApiModelProperty("获取章节列表规则")
    private String chapterList;

    @ApiModelProperty("获取章节名称的规则")
    private String chapterName;

    @ApiModelProperty("获取章节链接的规则")
    private String chapterUrl;

    @ApiModelProperty("编码")
    private String charsetName;

    @ApiModelProperty("下一页的规则")
    private String nextPage;

}
