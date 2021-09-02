package com.common.httplib.callback

import com.common.httplib.model.Progress




/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: ProgressCallback
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 9:30 上午
 *
 */
interface DownloadCallback {
    fun onProgress(progress: Progress)
//    fun onStart()
//    fun onFinish(path: String?) //下载完成
//    fun onFail(errorInfo: String?) //下载失败

}