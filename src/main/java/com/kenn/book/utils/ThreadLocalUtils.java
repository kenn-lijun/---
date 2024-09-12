package com.kenn.book.utils;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadLocalUtils {

    private static final ThreadLocal<String> BASE_URL_CACHE = new TransmittableThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_URL_CACHE = new TransmittableThreadLocal<>();
    // 每个线程调用get()时，如果没有值，就会调用这个方法初始化Map
    private static final ThreadLocal<Map<String, String>> HTTP_HEADER_CACHE = TransmittableThreadLocal.withInitial(ConcurrentHashMap::new);

    public static void setBaseUrlCache(String type) {
        BASE_URL_CACHE.set(type);
    }

    public static String getBaseUrl() {
        return BASE_URL_CACHE.get();
    }

    public static void setCurrentUrlCache(String type) {
        CURRENT_URL_CACHE.set(type);
    }

    public static String getCurrentUrl() {
        return CURRENT_URL_CACHE.get();
    }

    public static Map<String, String> getHeader() {
        return HTTP_HEADER_CACHE.get();
    }

    public static void addHeader(String key, String value) {
        Map<String, String> headerMap = HTTP_HEADER_CACHE.get();
        headerMap.put(key, value);
    }

    public static void clear() {
        BASE_URL_CACHE.remove();
        CURRENT_URL_CACHE.remove();
        HTTP_HEADER_CACHE.remove();
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private ThreadLocalUtils() {}

}
