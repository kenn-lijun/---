package com.kenn.book.utils;

import com.kenn.book.domain.OkHttpResult;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.eclipse.jetty.util.ssl.SslContextFactory.TRUST_ALL_CERTS;

public final class HttpsUtils {

    private HttpsUtils() {}

    private static OkHttpClient okHttpClient = null;

    static {
        try {
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, TRUST_ALL_CERTS, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpResult get(String url, String charset) {
        Request request = new Request.Builder()
                .url(url)
                .header("Cache-Control", "no-cache")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36")
                .build();
        return execNewCall(request, charset);
    }

    public static OkHttpResult postFormParams(String url, RequestBody formBody, String charset) {
        Request request = new Request.Builder()
                .url(url)
                .header("Cache-Control", "no-cache")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36")
                .post(formBody)
                .build();
        return execNewCall(request, charset);
    }

    private static OkHttpResult execNewCall(Request request, String charset){
        Response response;
        String data = "";
        Integer code = null;
        try {
            if (okHttpClient != null) {
                response = okHttpClient.newCall(request).execute();
                code = response.code();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    byte[] bytes = responseBody.bytes();
                    data = new String(bytes, charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OkHttpResult.builder().code(code).data(data).build();
    }

}