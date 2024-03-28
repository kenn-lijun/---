package com.kenn.book.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Description TODO
 * @ClassName MyBookShelf
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年05月21日 14:32:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("my_bookshelf")
@ApiModel(value = "我的书架",description = "我的书架字段相关描述")
public class MyBookShelf {

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

    @ApiModelProperty("最新章节链接")
    private String newChapterUrl;

}
