package com.kenn.book.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 正则工具类
 * @ClassName JsUtils
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年03月28日 11:48:00
 */
public final class RegexUtils {

    private RegexUtils() {}

    /**
     * 分割所有的js执行节点
     * @param input:
     * @return: java.util.List<java.lang.String>
     * @author: kenn
     * @date: 2024/6/19 10:49
     */
    public static List<String> splitJsNode(String input) {
        List<String> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile("(<js>[\\s\\S]*?</js>)|((?:(?!<js>)[\\s\\S])+)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            // group(0) 代表整个表达式匹配的内容
            // group(1) 代表第一个括号内的组
            if (StringUtils.isNotEmpty(matcher.group(1))) {
                // 添加<js>标签内的内容
                resultList.add(matcher.group(1));
            } else if (StringUtils.isNotEmpty(matcher.group(2))) {
                // 添加<js>标签外的内容
                resultList.add(matcher.group(2));
            }
        }
        return resultList;
    }

    /**
     * 获取用户配置的regex代码串 适用于字符串中只包含一个regex代码串的情况 如获取<js>aaa</js>中的aaa
     * @param ruleStr: 用户配置的规则信息
     * @return: java.lang.String
     * @author: kenn
     * @date: 2024/3/27 15:16
     */
    public static String getRegexCode(String ruleStr, String regexCode) {
        String result = null;
        Pattern pattern = Pattern.compile(regexCode);
        Matcher matcher = pattern.matcher(ruleStr);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

}
