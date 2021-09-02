package com.common.httplib.interceptor

import com.common.httplib.callback.DownloadCallback
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: DownloadInterceptor
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 11:19 上午
 *
 */
class DownloadInterceptor(private val downloadCallback: DownloadCallback) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val oriBody = originalResponse.body
        return if (oriBody != null) {
            originalResponse.newBuilder()
                .body(ProgressResponseBody(oriBody, downloadCallback))
                .build()
        } else {
            originalResponse
        }
    }
}