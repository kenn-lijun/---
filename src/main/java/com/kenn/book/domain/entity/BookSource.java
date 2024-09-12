package com.kenn.book.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description TODO
 * @ClassName BookSource
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月27日 16:31:00
 */
@Data
@TableName("book_source")
@ApiModel(value = "书源",description = "书源字段相关描述")
public class BookSource {

    @TableId
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("书源url")
    private String baseUrl;

    @ApiModelProperty("请求头(非必填)")
    private String header;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否删除：1: 已删除 0：未删除")
    @TableLogic
    private Integer isDelete;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
