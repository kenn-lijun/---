package com.kenn.book.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description TODO
 * @ClassName BrowseHistory
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年11月28日 09:49:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("browse_history")
@ApiModel(value = "浏览历史",description = "浏览历史字段相关描述")
public class BrowseHistory {

    @TableId
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("小程序openid")
    private String openid;

    @ApiModelProperty("书籍名称")
    private String bookName;

    @ApiModelProperty("书籍链接")
    private String bookLink;

    @ApiModelProperty("书源名称")
    private String source;

    @ApiModelProperty("图片链接")
    private String img;

    @ApiModelProperty("作者")
    private String author;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
