package com.kenn.book.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description TODO
 * @ClassName BookTypeResult
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年06月01日 15:59:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("发现页信息")
public class BookTypeResult {

    @ApiModelProperty("下一页链接")
    private String nextPageUrl;

    @ApiModelProperty("书籍列表")
    private List<SearchResult> bookList;

}
