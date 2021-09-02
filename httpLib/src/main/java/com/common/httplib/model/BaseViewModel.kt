package com.common.httplib.model

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.common.httplib.config.HttpConfig
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: BaseViewModel
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 7:10 下午
 *
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {
    suspend fun <T> httpRequest(request: suspend CoroutineScope.() -> BaseResponse<T>): BaseResponse<T> {
        return withContext(Dispatchers.IO) {
            val result: BaseResponse<T>
            try {
                result = request()
            } catch (e: Throwable) {
//                return@withContext ApiExceptionHandler.build(e).toResponse<T>()
                return@withContext if (e is HttpException) {
                    BaseResponse(HttpConfig.CODE_NET_ERROR, "网络异常(${e.code()},${e.message()})")
                } else if (e is UnknownHostException) {
                    BaseResponse(HttpConfig.CODE_NET_ERROR, "网络连接失败，请检查后再试")
                } else if (e is ConnectTimeoutException || e is SocketTimeoutException) {
                    BaseResponse(HttpConfig.CODE_TIMEOUT, "请求超时，请稍后再试")
                } else if (e is IOException) {
                    BaseResponse(HttpConfig.CODE_NET_ERROR, "网络异常(${e.message})")
                } else if (e is JsonParseException || e is JSONException) {
                    // Json解析失败
                    BaseResponse(HttpConfig.CODE_JSON_PARSE_ERROR, "数据解析错误，请稍后再试")
                } else {
                    BaseResponse(HttpConfig.CODE_SERVER_ERROR, "系统错误(${e.message})")
                }
            }

            if (result.code == HttpConfig.LOG_TIME_OUT) {
//                登录超时,取消协程，跳转登录界面
                cancel()
            }
            return@withContext result
        }
    }
}