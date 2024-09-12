package com.kenn.book.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.res.OkHttpResult;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @Description 供于用户书源中自定义js使用 请勿删除 请勿删除 请勿删除
 * @ClassName JavaUtils
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年06月20日 17:06:00
 */
public final class JavaUtils {

    private JavaUtils() {}

    public static String ajaxGet(String url) {
        OkHttpResult okHttpResult = HttpsUtils.get(url, Constants.UTF8);
        return HttpsUtils.judgeResult(okHttpResult) ? okHttpResult.getData() : null;
    }

    public static String bytes2Str(byte[] bytes) {
        return new String(bytes);
    }

    public static byte[] str2Bytes(String input) {
        return input.getBytes();
    }

    public static String md5Encode(String input) {
        return SecureUtil.md5(input);
    }

    public static String md5Encode16(String input) {
        String result = md5Encode(input);
        return result.substring(8, 24);
    }

    public static String base64Decode(String input) {
        return Base64.decodeStr(input);
    }

    public static String base64Decode(String input, String charset) {
        return Base64.decodeStr(input, Charset.forName(charset));
    }

    /* HexString 解码为字节数组 */
    public static byte[] hexDecodeToByteArray(String hex) {
        return HexUtil.decodeHex(hex);
    }

    /* hexString 解码为utf8 */
    public static String hexDecodeToString(String hex) {
        return HexUtil.decodeHexStr(hex);
    }

    /* utf8 编码为hexString */
    public static String hexEncodeToString(String utf8) {
        return HexUtil.encodeHexStr(utf8);
    }

    /**
     * utf8编码转gbk编码
     */
    public static String utf8ToGbk(String input) {
        byte[] utf8Bytes = input.getBytes(StandardCharsets.UTF_8);
        return new String(utf8Bytes, Charset.forName("GBK"));
    }

    public static String encodeURI(String input) {
        String result = "";
        try {
            result = URLEncoder.encode(input, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encodeURI(String input, String charset) {
        String result = "";
        try {
            result = URLEncoder.encode(input, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成UUID
     */
    public static String  randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static void addHeader(String key, String value) {
        ThreadLocalUtils.addHeader(key, value);
    }

    public static void addHeader(String headerJson) throws JsonProcessingException {
        if (StringUtils.isNotEmpty(headerJson)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> headerMap = objectMapper.readValue(headerJson, new TypeReference<Map<String, String>>() {});
            headerMap.forEach(JavaUtils::addHeader);
        }
    }

    public static String timeFormat(Long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

}
