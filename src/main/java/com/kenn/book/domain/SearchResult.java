package com.kenn.book.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * date: 2021/7/1 8:49
 *
 * @author 18305
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "书籍搜索相关字段")
public class SearchResult {

    @ApiModelProperty("书名")
    private String name;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("最新章节名称")
    private String newChapter;

    @ApiModelProperty("书籍链接")
    private String link;

    @ApiModelProperty("最新章节链接")
    private String newChapterLink;

    @ApiModelProperty("更新时间")
    private String updateTime;

    @ApiModelProperty("封面链接")
    private String imgLink;

}
