package com.kenn.book.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description TODO
 * @ClassName InfoSearchRule
 * @Author kenn
 * @Version 1.0.0
 * @Date 2023年09月30日 10:05:00
 */
@Data
@TableName("info_search_rule")
@ApiModel(value = "根据章节链接获取详情规则")
public class InfoSearchRule {

    @TableId
    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("书源id")
    private String sourceId;

    @ApiModelProperty("源基础url")
    private String baseUrl;

    @ApiModelProperty("编码")
    private String charsetName;

    @ApiModelProperty("获取章节名称的规则")
    private String name;

    @ApiModelProperty("获取详情的规则")
    private String info;

    @ApiModelProperty("下一章链接规则")
    private String nextUrl;

    @ApiModelProperty("上一章链接规则")
    private String beforeUrl;

    @ApiModelProperty("下一页链接")
    private String nextPage;

}
