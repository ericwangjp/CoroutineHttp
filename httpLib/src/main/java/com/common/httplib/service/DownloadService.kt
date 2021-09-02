package com.common.httplib.service

import com.common.httplib.model.BaseResponse
import okhttp3.RequestBody
import retrofit2.http.*


/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: DownloadService
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 1:49 下午
 *
 */
interface DownloadService {

    @Multipart
    @POST("user/photo")
    suspend fun <T> uploadFile(
        @Part("photo") photo: RequestBody?,
        @Part("description") description: RequestBody?
    ): BaseResponse<T>

    @Streaming
    @GET("user/photo")
    suspend fun <T> downloadFile(
        @Part("photo") photo: RequestBody?,
        @Part("description") description: RequestBody?
    ): BaseResponse<T>
}