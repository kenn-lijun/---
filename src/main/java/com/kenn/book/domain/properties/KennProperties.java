package com.kenn.book.domain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kenn")
public class KennProperties {

    private String signKey;

    private String[] excludes;

}