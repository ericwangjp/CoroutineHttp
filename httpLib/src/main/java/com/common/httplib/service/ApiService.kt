package com.common.httplib.service

import com.common.httplib.base.BaseResponse
import com.common.httplib.model.ResultResp
import okhttp3.ResponseBody
import retrofit2.Response
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

    @GET("getJoke?page=1&count=2&type=video")
//    suspend fun getArticleById(@Path("id") id: Long): BaseResponse<String>
    suspend fun  searchSongs(@Query("keywords") keywords: String): BaseResponse<List<ResultResp>>

    //    @Headers("Cache-Control: max-age=640000")
//    @Headers({
//        "Accept: application/vnd.github.v3.full+json",
//        "User-Agent: Retrofit-Sample-App"
//    })
    @FormUrlEncoded
    @POST("user/edit")
    suspend fun updateUser(
        @Field("first_name") first: String?,
        @Field("last_name") last: String?
    ): BaseResponse<List<ResultResp>>
}