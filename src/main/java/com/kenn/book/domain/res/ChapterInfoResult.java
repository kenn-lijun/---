package com.kenn.book.domain.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @ClassName ChapterInfo
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年03月16日 16:38:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("章节详情信息")
public class ChapterInfoResult {

    @ApiModelProperty("章节名称")
    private String name;

    @ApiModelProperty("章节链接")
    private String link;

}
