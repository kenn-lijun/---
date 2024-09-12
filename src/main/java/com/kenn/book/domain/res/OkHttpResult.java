package com.kenn.book.domain.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @ClassName OkHttpResult
 * @Author kenn
 * @Version 1.0.0
 * @Date 2022年03月23日 14:13:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OkHttpResult {

    private Integer code;

    private String data;

    private String responseType;

}
