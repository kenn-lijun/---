package com.kenn.book.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description TODO
 * @ClassName InfoPurifyRule
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年06月26日 14:17:00
 */
@Data
@TableName("info_purify_rule")
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoPurifyRule {

    @TableId
    private Long id;

    private String name;

    private String pattern;

    private String replacement;

    private Boolean isRegex;

    private Boolean isEnabled;

    @JsonProperty("order")
    private Integer sort;

}
