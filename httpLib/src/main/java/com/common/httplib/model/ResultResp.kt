package com.common.httplib.model

import com.common.httplib.base.BaseResponse

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: Jokes
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/4 10:35 下午
 *
 */
data class ResultResp(
    val comment: String,
    val down: String,
    val forward: String,
    val header: String,
    val images: Any,
    val name: String,
    val passtime: String,
    val sid: String,
    val text: String,
    val thumbnail: String,
    val top_comments_content: String,
    val top_comments_header: String,
    val top_comments_name: String,
    val top_comments_uid: String,
    val top_comments_voiceuri: String,
    val type: String,
    val uid: String,
    val up: String,
    val video: String
)
