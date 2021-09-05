package com.common.httplib.model

import android.net.Uri

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: DownloadStatus
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/3 5:05 下午
 *
 */
sealed class DownloadStatus {
    class DownloadProcess(val progress: Progress) :
        DownloadStatus()

    class DownloadError(val t: Throwable) : DownloadStatus()
    class DownloadSuccess(val uri: Uri) : DownloadStatus()
}
