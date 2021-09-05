package com.common.httplib.utils

import android.content.Context
import android.util.Log
import com.common.httplib.config.HttpConfig
import com.common.httplib.interceptor.DownloadInterceptor
import com.common.httplib.interceptor.HttpLogInterceptor
import com.common.httplib.model.Progress
import com.common.httplib.service.ApiService
import com.common.httplib.service.DownloadService
import kotlinx.coroutines.CoroutineScope
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
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

    fun getDownloadService(
        coroutine: CoroutineScope? = null,
        progress: (suspend (Progress) -> Unit)? = null
    ): DownloadService {
        val downLoadClient = OkHttpClient.Builder()
            .connectTimeout(HttpConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(HttpConfig.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(DownloadInterceptor(coroutine, progress))
            .build()
        return Retrofit.Builder()
            .baseUrl(HttpConfig.HTTP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(downLoadClient)
            .build().create(DownloadService::class.java)
    }

    /**
     * 将文件转换为 MultipartBody.Part 集合形式
     *
     * @param key       后台服务器的字段名
     * @param filePaths
     * @return
     */
    fun filesToMultipartBodyParts(
        key: String,
        filePaths: List<String>
    ): List<MultipartBody.Part?>? {
        return if (filePaths.isNotEmpty()) {
            val parts: MutableList<MultipartBody.Part?> = ArrayList(filePaths.size)
            for (filePath in filePaths) {
                val file = File(filePath)
                //这里的image/*表示上传的是图片的所有格式，可以替换成需要的格式
                val requestBody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                var part: MultipartBody.Part? = null
                try {
                    part = MultipartBody.Part.createFormData(
                        key,
                        URLEncoder.encode(file.name.replace(" ", ""), HttpConfig.ENCODE_TYPE_UTF8),
                        requestBody
                    )
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                if (part != null) {
                    parts.add(part)
                }
            }
            parts
        } else {
            null
        }
    }

    /**
     * 将文件转换为 MultipartBody.Part 形式
     *
     * @param key      后台服务器的字段名
     * @param filePath
     * @return
     */
    fun convertFileToMultipartBody(key: String?, filePath: String): MultipartBody.Part? {
        var key = key
        if (filePath.isNotEmpty()) {
            val file = File(filePath)
            //这里的image/*表示上传的是图片的所有格式，可以替换成需要的格式
            val requestBody: RequestBody =
                file.asRequestBody(HttpConfig.MEDIA_TYPE_FILE.toMediaTypeOrNull())
            if (key.isNullOrEmpty()) {
                key = file.name
            }
            try {
                return MultipartBody.Part.createFormData(
                    key!!,
                    URLEncoder.encode(file.name.replace(" ", ""), HttpConfig.ENCODE_TYPE_UTF8),
                    requestBody
                )
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 将file转为 RequestBody 类型
     *
     * @param file
     * @return
     */
    fun convertToRequestBody(file: File?): RequestBody? {
        return file?.asRequestBody(HttpConfig.MEDIA_TYPE_STREAM.toMediaTypeOrNull())
    }

    /**
     * 将json参数转为 RequestBody 类型
     *
     * @param json
     * @return
     */
    fun convertToRequestBody(json: String?): RequestBody {
        val requestJson = JSONObject().apply {
            //            requestJson.put("requestId", RandomUtil.getCharAndNumr(18))
//            requestJson.put("deviceMod", DeviceUtils.getDeviceModel())
//            requestJson.put("deviceId", DeviceUtils.getUniqueID(mContext))
//            requestJson.put("opSys", "1")
//            requestJson.put("opSysVer", DeviceUtils.getSystemVersion())
//            requestJson.put("clientVer", AppUtils.getVersionName(mContext))
//            requestJson.put("token", UserInfoUtil.getSavedToken())
            json?.let { put("reqData", JSONObject(json)) }
        }

        Log.d("请求明文参数", requestJson.toString())
        return requestJson.toString()
            .toRequestBody(HttpConfig.MEDIA_TYPE_JSON.toMediaTypeOrNull())
    }

}