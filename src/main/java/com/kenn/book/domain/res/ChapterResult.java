package com.kenn.book.domain.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * date: 2021/7/1 10:35
 *
 * @author 18305
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("章节列表信息")
public class ChapterResult {

    @ApiModelProperty("当前第几页：小程序限制, 页面不能超过一定大小限制, 如果章节太多的情况下分页返回")
    private Integer currentPage;

    @ApiModelProperty("总共多少条数据")
    private Integer total;

    @ApiModelProperty("简介")
    private String introduce;

    @ApiModelProperty("封面链接")
    private String img;

    @ApiModelProperty("章节列表")
    List<ChapterInfoResult> chapterList;

}
