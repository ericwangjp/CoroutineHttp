package com.common.httplib.interceptor

import android.content.Context
import com.common.httplib.config.HttpConfig

import okhttp3.CacheControl

import okhttp3.Interceptor
import okhttp3.Request


/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: HttpCacheInterceptor
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/1 5:11 下午
 *
 */
object HttpCacheInterceptor {

    /**
     * 在无网络的情况下读取缓存，有网络的情况下根据缓存的过期时间重新请求,
     *
     * @return
     */
    fun getCacheInterceptor(context: Context?): Interceptor {
        return Interceptor { chain ->
            var request: Request = chain.request()
            // TODO: 2021/9/1  
//            if (!NetworkUtils.checkNet(context)) {
            if (false) {
                //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
            val response = chain.proceed(request)
//            if (NetworkUtils.checkNet(context)) { //有网络情况下，根据请求接口的设置，配置缓存。
            if (true) { //有网络情况下，根据请求接口的设置，配置缓存。
                //这样在下次请求时，根据缓存决定是否真正发出请求。
                val cacheControl: String = request.cacheControl.toString()
                //当然如果你想在有网络的情况下都直接走网络，那么只需要
                //将其超时时间这是为0即可:String cacheControl="Cache-Control:public,max-age=0"
                response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    //                            自定义缓存策略
                    //                            .header("Cache-Control", "public, max-age=" + HttpConfig.MAX_CACHE_TIME_WITH_NET)// read from cache for 1 minute
                    .removeHeader("Pragma")
                    .build()
            } else {
                //无网络
                response.newBuilder()
                    .header(
                        "Cache-Control",
                        "public,only-if-cached,max-stale=" + HttpConfig.MAX_CACHE_TIME_WITHOUT_NET
                    ) // tolerate 24-hours stale
                    .removeHeader("Pragma")
                    .build()
            }
        }
    }
}