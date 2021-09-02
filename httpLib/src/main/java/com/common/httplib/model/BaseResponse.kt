package com.common.httplib.model

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: BaseResponse
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/1 2:10 下午
 *
 */
data class BaseResponse<T>(var code: String, var message: String?, var data: T? = null)
