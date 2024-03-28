package com.kenn.book.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @Description TODO
 * @ClassName JsUtils
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年03月28日 11:48:00
 */
public final class JsUtils {

    private JsUtils() {}

    private static String execute() {

        ThreadLocalUtils.setCache("哈哈哈");

        String result = null;
        // 创建一个ScriptEngineManager对象
        ScriptEngineManager manager = new ScriptEngineManager();
        // 获取JavaScript引擎
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        engine.put("baseUrl", "http://www.baidu.com/book/1000.html");
        engine.put("relativeUrl", "/1000/65535.html");

//        String templateCode = "function getAbsoluteUrl() {" +
//                    "   var subStr = baseUrl.substring(0, baseUrl.lastIndexOf('/'));" +
//                    "   return subStr + relativeUrl;" +
//                "}" +
//                "getAbsoluteUrl()";

        String templateCode = "var subStr = baseUrl.substring(0, baseUrl.lastIndexOf('/'));" +
                "var ThreadLocalUtils = Java.type('com.kenn.book.utils.ThreadLocalUtils');" +
                "var cache = ThreadLocalUtils.getCache();" +
                "var result = subStr + relativeUrl + cache;";

        try {
            // 执行js代码片段
            engine.eval(templateCode);
            result = engine.get("result").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ThreadLocalUtils.clear();
        return result;
    }

    public static void main(String[] args) {
        System.out.println(execute());
    }

}
