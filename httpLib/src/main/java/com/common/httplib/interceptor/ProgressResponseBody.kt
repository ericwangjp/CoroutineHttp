package com.common.httplib.interceptor

import android.os.SystemClock
import com.common.httplib.model.Progress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*


/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: ProgressResponseBody
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/1 9:42 下午
 *
 */
class ProgressResponseBody(
    private var responseBody: ResponseBody,
    private val coroutine: CoroutineScope? = null,
    private val progress: (suspend (Progress) -> Unit)? = null
) : ResponseBody() {

    private var lastRefreshTime: Long = 0
    private var lastProgress: Int = 0
    private val updateInterval = 600

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength() ?: 0
    }

    override fun source(): BufferedSource {
        return mSource(responseBody.source()).buffer()
    }

    private fun mSource(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                // 进度回调
                val currentTime = SystemClock.elapsedRealtime()
                val isUpdate = currentTime - lastRefreshTime >= updateInterval
                val curProgress = (totalBytesRead * 100 / responseBody.contentLength()).toInt()
                if (isUpdate || (curProgress > lastProgress)) {
                    lastRefreshTime = currentTime
                    lastProgress = curProgress
                    coroutine?.launch {
                        // 主线程
                        val updateProgress =
                            Progress(lastProgress, totalBytesRead, responseBody.contentLength())
                        progress?.let { it(updateProgress) }
                    }

                }
                return bytesRead
            }
        }
    }
}