package com.common.httplib.utils

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: CommonUtils
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 5:16 下午
 *
 */

//suspend inline fun <T> httpRequest(crossinline request: suspend CoroutineScope.() -> BaseResponse<T>): BaseResponse<T> {
//    return withContext(Dispatchers.IO) {
//        val result: BaseResponse<T>
//        try {
//            result = request()
//        } catch (e: Throwable) {
//            return@withContext ApiExceptionHandler.build(e).toResponse<T>()
//        }
//
//        if (result.code == HttpConfig.LOG_TIME_OUT) {
////                登录超时,取消协程，跳转登录界面
//            cancel()
//        }
//        return@withContext result
//    }
//}


