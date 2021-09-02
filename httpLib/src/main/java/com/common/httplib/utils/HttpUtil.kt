package com.common.httplib.utils

import android.content.Context
import com.common.httplib.callback.DownloadCallback
import com.common.httplib.config.HttpConfig
import com.common.httplib.interceptor.DownloadInterceptor
import com.common.httplib.interceptor.HttpLogInterceptor
import com.common.httplib.model.BaseResponse
import com.common.httplib.service.ApiService
import com.common.httplib.service.DownloadService
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import okhttp3.*
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: HttpUtil
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/1 2:50 下午
 *
 */
object HttpUtil {

    private lateinit var retrofit: Retrofit

    fun initHttp(mContext: Context, isDebug: Boolean) {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(HttpConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(HttpConfig.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
//        .addInterceptor(DataEncryptInterceptor())  // 数据加解密
        if (isDebug) {
            okHttpClientBuilder.addNetworkInterceptor(HttpLogInterceptor.getHttpLogInterceptor())
        }
        //创建Cache
        val httpCacheDirectory = File(mContext.cacheDir, "OkHttpCache")
        val cache = Cache(httpCacheDirectory, HttpConfig.MAX_CACHE_SIZE)
        okHttpClientBuilder.cache(cache)
//            .addNetworkInterceptor(HttpCacheInterceptor.getCacheInterceptor(mContext))

//        cookie持久化
//            clientBuilder.addInterceptor(new AddCookiesInterceptor());
//            clientBuilder.addInterceptor(new SaveCookiesInterceptor());
//            cookie持久化  'com.squareup.okhttp3:okhttp-urlconnection:3.5.0'
//            CookieHandler cookieHandler = new CookieManager(
//                    new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
//            builder.cookieJar(new JavaNetCookieJar(cookieHandler));
//            cookie持久化方式2  'com.github.franmontiel:PersistentCookieJar:v1.0.0'
//            ClearableCookieJar cookieJar = new PersistentCookieJar(
//                    new SetCookieCache(), new SharedPrefsCookiePersistor(context));
//            builder.cookieJar(cookieJar);
//        cookie非持久化
        okHttpClientBuilder.cookieJar(object : CookieJar {
            private val cookieStore: HashMap<HttpUrl, List<Cookie>> = HashMap()
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val cookies = cookieStore[url] //取出cookie
                return cookies ?: ArrayList()
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url] = cookies;//保存cookie
            }

        })
        val okHttpClient = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(HttpConfig.HTTP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun <T> getRequestService(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun getDownloadService(callback: DownloadCallback): DownloadService {
        val downLoadClient = OkHttpClient.Builder()
            .connectTimeout(HttpConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(HttpConfig.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(DownloadInterceptor(callback))
            .build()
        return Retrofit.Builder()
            .baseUrl(HttpConfig.HTTP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(downLoadClient)
            .build().create(DownloadService::class.java)
    }

}