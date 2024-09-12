package com.kenn.book.utils;

import cn.hutool.core.collection.CollUtil;
import com.kenn.book.domain.Constants;
import com.kenn.book.domain.HttpStatus;
import com.kenn.book.domain.res.OkHttpResult;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class HttpsUtils {

    private HttpsUtils() {}

    private static OkHttpClient okHttpClient;

    static {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            okHttpClient = new OkHttpClient.Builder().build();
        }
    }

    public static OkHttpResult get(String url, String charset) {
        Request.Builder requestBuilder = getRequestBuilder();
        Request request = requestBuilder.url(url).build();
        return execNewCall(request, charset);
    }

    public static OkHttpResult postFormParams(String url, RequestBody formBody, String charset) {
        Request.Builder requestBuilder = getRequestBuilder();
        Request request = requestBuilder.url(url).post(formBody).build();
        return execNewCall(request, charset);
    }

    public static boolean judgeResult(OkHttpResult result) {
        return result != null && result.getCode() != null && result.getCode().equals(HttpStatus.SUCCESS) && StringUtils.isNotEmpty(result.getData());
    }

    private static OkHttpResult execNewCall(Request request, String charset){
        Response response;
        String data = "";
        Integer code = null;
        String responseType = null;
        try {
            if (okHttpClient != null) {
                response = okHttpClient.newCall(request).execute();
                code = response.code();

                String contentType = response.header("Content-Type");
                if (contentType != null) {
                    if (contentType.contains("application/json")) {
                        // 处理JSON响应
                        responseType = Constants.RULE_TYPE_JSON;
                    } else if (contentType.contains("text/html")) {
                        // 处理HTML响应
                        responseType = Constants.RULE_TYPE_HTML;
                    } else {
                        // 其他类型的响应
                        responseType = Constants.RULE_TYPE_UNKNOWN;
                    }
                }
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    byte[] bytes = responseBody.bytes();
                    data = new String(bytes, charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OkHttpResult.builder().code(code).responseType(responseType).data(data).build();
    }

    private static Request.Builder getRequestBuilder() {
        Request.Builder requestBuilder = new Request.Builder()
                .header("Cache-Control", "no-cache")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
        Map<String, String> header = ThreadLocalUtils.getHeader();
        if (CollUtil.isNotEmpty(header)) {
            header.forEach(requestBuilder::header);
        }
        return requestBuilder;
    }

}