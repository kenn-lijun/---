package com.kenn.book.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description TODO
 * @ClassName SearchHistory
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月26日 16:35:00
 */
@Data
@TableName("search_history")
@ApiModel(value = "搜索历史",description = "搜索历史字段相关描述")
public class SearchHistory {

    @TableId
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("小程序openid")
    private String openid;

    @ApiModelProperty("搜索信息")
    private String info;

    @ApiModelProperty("书源")
    private String source;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
