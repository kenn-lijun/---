package com.kenn.book.utils;

import cn.hutool.core.util.ObjectUtil;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description TODO
 * @ClassName JsUtils
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年03月28日 11:48:00
 */
public final class JsUtils {

    private JsUtils() {}

    /**
     * 执行js代码串
     * @param code:	js代码
     * @param result: js全局变量
     * @return: java.lang.String
     * @author: kenn
     * @date: 2024/6/20 18:17
     */
    public static String execute(String code, String result) {
        String executeResult = null;
        try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
            // 将result变量传递给JavaScript环境
            context.getBindings("js").putMember("result", result);
            context.getBindings("js").putMember("baseUrl", ThreadLocalUtils.getBaseUrl());
            context.getBindings("js").putMember("currentUrl", ThreadLocalUtils.getCurrentUrl());
            // 执行js代码片段
            context.eval("js", "var ThreadLocalUtils = Java.type('com.kenn.book.utils.ThreadLocalUtils');");
            context.eval("js", "var java = Java.type('com.kenn.book.utils.JavaUtils');");
            // 在这里执行您的JavaScript代码
            Value eval = context.eval("js", code);

            if (!eval.isNull()) {
                executeResult = eval.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeResult;
    }

    public static String executePage(String code) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String result = null;
        try {
            Object eval = engine.eval(code);
            if (ObjectUtil.isNotNull(eval)) {
                result = eval.toString();
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 分割所有的执行节点
     * @param input:
     * @return: java.util.List<java.lang.String>
     * @author: kenn
     * @date: 2024/6/19 10:49
     */
    public static List<String> splitExecuteNode(String input) {
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

    public static String pageExecuteNode(String input) {
        String result = null;
        Pattern pattern = Pattern.compile("(<page>[\\s\\S]*?</page>)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    /**
     * 获取用户配置的js代码串 适用于字符串中只包含一个js代码串的情况
     * @param ruleStr: 用户配置的规则信息
     * @return: java.lang.String
     * @author: kenn
     * @date: 2024/3/27 15:16
     */
    public static String getJsCode(String ruleStr) {
        String result = null;
        Pattern pattern = Pattern.compile("<js>([\\s\\S]*?)</js>");
        Matcher matcher = pattern.matcher(ruleStr);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public static String getPageCode(String ruleStr) {
        String result = null;
        Pattern pattern = Pattern.compile("<page>([\\s\\S]*?)</page>");
        Matcher matcher = pattern.matcher(ruleStr);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

}
