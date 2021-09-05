package com.common.httplib.base

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: BaseRepository
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 9:39 下午
 *
 */
open class BaseRepository {
    suspend fun <T> httpRequest(request: suspend CoroutineScope.() -> T): T? {
        return withContext(Dispatchers.IO) {
            // 子线程
            request()
//            request.invoke()
        }.apply {
            Log.e("接口返回数据：", "----------> ${this?.toString()}")
//            when (errorCode) {
//                0, 200 -> this
//                100, 401 -> ""
//                403 -> ""
//                404 -> ""
//                500 -> ""
//                else -> ""
//            }
        }
    }

//    suspend fun <T> httpDownload(request: suspend CoroutineScope.() -> Response<T>): Response<T>? {
//        return withContext(Dispatchers.IO) {
//            var result: Response<T>? = null
//            Log.e("BaseRepository，当前线程", Thread.currentThread().name)
//            try {
//                result = request()
//            } catch (e: Throwable) {
//                Log.e("出错了", "==>${e.cause.toString()}")
////                return@withContext if (e is HttpException) {
////                    Response(HttpConfig.CODE_NET_ERROR, "网络异常(${e.code()},${e.message()})")
////                } else if (e is UnknownHostException) {
////                    Response(HttpConfig.CODE_NET_ERROR, "网络连接失败，请检查后再试")
////                } else if (e is ConnectTimeoutException || e is SocketTimeoutException) {
////                    Response(HttpConfig.CODE_TIMEOUT, "请求超时，请稍后再试")
////                } else if (e is IOException) {
////                    Response(HttpConfig.CODE_NET_ERROR, "网络异常(${e.message})")
////                } else if (e is JsonParseException || e is JSONException) {
////                    // Json解析失败
////                    Response(HttpConfig.CODE_JSON_PARSE_ERROR, "数据解析错误，请稍后再试")
////                } else {
////                    Response(HttpConfig.CODE_SERVER_ERROR, "系统错误(${e.message})")
////                }
//            }
//
////            if (result.code == HttpConfig.LOG_TIME_OUT) {
//////                登录超时,取消协程，跳转登录界面
////                cancel()
////            }
//            return@withContext result
//        }
//    }

}