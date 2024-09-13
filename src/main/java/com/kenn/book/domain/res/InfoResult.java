package com.kenn.book.domain.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * date: 2021/7/1 10:52
 *
 * @author 18305
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("阅读页相关信息")
public class InfoResult {

    @ApiModelProperty("章节名称")
    private String name;

    @ApiModelProperty("章节内容")
    private String info;

}
