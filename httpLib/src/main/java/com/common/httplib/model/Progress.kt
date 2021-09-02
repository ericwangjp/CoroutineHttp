package com.common.httplib.model

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: progress
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 9:22 上午
 *
 */
data class Progress constructor(
    private var progress: Int = 0,
    private var currentSize: Long = 0,
    private var totalSize: Long = 0
)