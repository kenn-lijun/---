package com.kenn.book.utils;

public class ThreadLocalUtils {

    private static final ThreadLocal<String> CACHE = new ThreadLocal<>();

    public static void setCache(String type) {
        CACHE.set(type);
    }

    public static String getCache() {
        return CACHE.get();
    }

    public static void clear() {
        CACHE.remove();
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private ThreadLocalUtils() {
    }

}
