package com.common.httplib.service

import com.common.httplib.model.BaseResponse
import retrofit2.http.*


/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: ApiService
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/1 1:56 下午
 *
 */
interface ApiService {

    @GET("search")
//    suspend fun getArticleById(@Path("id") id: Long): BaseResponse<String>
    suspend fun <T> searchSongs(@Query("keywords") keywords: String): BaseResponse<T>

    //    @Headers("Cache-Control: max-age=640000")
//    @Headers({
//        "Accept: application/vnd.github.v3.full+json",
//        "User-Agent: Retrofit-Sample-App"
//    })
    @FormUrlEncoded
    @POST("user/edit")
    suspend fun <T> updateUser(
        @Field("first_name") first: String?,
        @Field("last_name") last: String?
    ): BaseResponse<T>
}