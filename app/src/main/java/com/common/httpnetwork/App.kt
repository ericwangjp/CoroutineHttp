package com.common.httpnetwork

import android.app.Application
import com.common.httplib.utils.HttpUtil

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: App
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/1 7:47 下午
 *
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        HttpUtil.initHttp(this, true)
    }
}