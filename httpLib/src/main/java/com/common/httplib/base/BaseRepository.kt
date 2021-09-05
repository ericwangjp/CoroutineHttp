package com.common.httplib.base

import android.util.Log
import com.common.httplib.config.HttpConfig
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import okio.IOException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
            var result: T? = null
            Log.e("BaseRepository，当前线程", Thread.currentThread().name)  // 子线程
            try {
                result = request()
            } catch (e: Throwable) {
                Log.e("BaseRepository出错了", e.stackTraceToString())
                return@withContext if (e is HttpException) {
                    if (result is BaseResponse<*>) {
                        (result as BaseResponse<*>).code = HttpConfig.CODE_NET_ERROR
                        (result as BaseResponse<*>).message =
                            "网络异常(${e.code()},${e.message()})"
                        result
                    } else {
                        null
                    }
                } else if (e is UnknownHostException) {
                    if (result is BaseResponse<*>) {
                        (result as BaseResponse<*>).code = HttpConfig.CODE_NET_ERROR
                        (result as BaseResponse<*>).message = "网络连接失败，请检查后再试"
                        result
                    } else {
                        null
                    }
                } else if (e is ConnectTimeoutException || e is SocketTimeoutException) {
                    if (result is BaseResponse<*>) {
                        (result as BaseResponse<*>).code = HttpConfig.CODE_TIMEOUT
                        (result as BaseResponse<*>).message = "请求超时，请稍后再试"
                        result
                    } else {
                        null
                    }
                } else if (e is IOException) {
                    if (result is BaseResponse<*>) {
                        (result as BaseResponse<*>).code = HttpConfig.CODE_NET_ERROR
                        (result as BaseResponse<*>).message = "网络异常(${e.message})"
                        result
                    } else {
                        null
                    }
                } else if (e is JsonParseException || e is JSONException) {
                    // Json解析失败
                    if (result is BaseResponse<*>) {
                        (result as BaseResponse<*>).code = HttpConfig.CODE_JSON_PARSE_ERROR
                        (result as BaseResponse<*>).message = "数据解析错误，请稍后再试"
                        result
                    } else {
                        null
                    }
                } else {
                    if (result is BaseResponse<*>) {
                        (result as BaseResponse<*>).code = HttpConfig.CODE_SERVER_ERROR
                        (result as BaseResponse<*>).message = "系统错误(${e.message})"
                        result
                    } else {
                        null
                    }
                }
            }


            result.apply {
                if (result is BaseResponse<*>) {
                    val baseResp = result as BaseResponse<*>
                    if (baseResp.code == HttpConfig.LOG_TIME_OUT) {
                        //                登录超时,取消协程，跳转登录界面
                        cancel()
                    }
                }

            }
            return@withContext result
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