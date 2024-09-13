package com.kenn.book.domain;

/**
 * @author kenn
 * @version 1.0.0
 * @Description TODO
 * @ClassName Constans
 * @date 2022年03月11日 09:34:00
 */
public class Constants {

    public final static String UTF8 = "UTF-8";

    public final static String TIMESTAMP_KEY = "timestamp";

    public final static String SIGN_KEY = "sign";

    public final static String RULE_TYPE_HTML = "html";

    public final static String RULE_TYPE_JSON = "json";

    public final static String RULE_TYPE_UNKNOWN = "unknown";

    public final static String JS_REGEX_TAG = "<js>([\\s\\S]*?)</js>";

    public final static String PAGE_REGEX_TAG = "<page>([\\s\\S]*?)</page>";

    public final static String JOIN_REGEX_TAG = "<join>([\\s\\S]*?)</join>";

    public final static String WECHAT_LOGIN_BASE_URL = "https://api.weixin.qq.com/sns/jscode2session";

    public final static int FLAG_TRUE = 1;

    public static final String USER_LOCK_KEY = "user-lock-%s";

    public static final String SEARCH_HISTORY_LOCK_KEY = "search-history-lock-%s";

    public static final String BROWSE_HISTORY_LOCK_KEY = "browse-history-lock-%s";

}
