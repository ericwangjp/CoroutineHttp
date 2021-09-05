package com.common.httplib.service

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
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
    suspend fun uploadFile(
        @Part("photo") photo: RequestBody?,
        @Part("description") description: RequestBody?
    ): Response<ResponseBody>


    //    https://imtt.dd.qq.com/16891/apk/708730C7F45D042B2EFF1738D4F49FE7.apk?fsname=com.tencent.mobileqq_8.8.23_2034.apk&csr=9dcc
    @Streaming
//    @GET("708730C7F45D042B2EFF1738D4F49FE7.apk?fsname=com.tencent.mobileqq_8.8.23_2034.apk&csr=9dcc")
    @GET
    suspend fun downloadFile(
        @Url url: String
//        @Part("photo") photo: RequestBody?,
//        @Part("description") description: RequestBody?
    ): Response<ResponseBody>
}