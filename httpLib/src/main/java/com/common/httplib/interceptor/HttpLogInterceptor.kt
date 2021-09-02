package com.common.httplib.interceptor


import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor


/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: HttpLogInterceptor
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/1 4:51 下午
 *
 */
object HttpLogInterceptor {

    private const val TAG = "『==>HttpNetworkLog<==』"

    fun getHttpLogInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor {
            // TODO: 2021/9/1
            Log.d(TAG, "☞=== \n$it\n ===☜")
        }
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }
}