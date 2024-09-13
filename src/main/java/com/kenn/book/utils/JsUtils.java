package com.kenn.book.utils;

import cn.hutool.core.util.ObjectUtil;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @Description js代码引擎工具类
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

}
