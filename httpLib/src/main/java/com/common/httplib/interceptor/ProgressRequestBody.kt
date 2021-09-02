package com.common.httplib.interceptor

import com.common.httplib.callback.DownloadCallback
import com.common.httplib.model.Progress
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*


/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: 文件上传进度
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/1 9:29 下午
 *
 */
class ProgressRequestBody constructor(
    private var requestBody: RequestBody,
    private var callback: DownloadCallback?
) : RequestBody() {

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (sink is Buffer
            || sink.toString().contains(
                "com.android.tools.profiler.support.network.HttpTracker\$OutputStreamTracker"
            )
        ) {
            requestBody.writeTo(sink)
        } else {
            val bufferedSink = sink(sink).buffer()
            requestBody.writeTo(bufferedSink)
            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink.flush()
//            bufferedSink.close()
        }
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            var bytesWritten = 0L

            //总字节长度，避免多次调用contentLength()方法
            var contentLength = 0L

            // 当前进度值，防止回调频繁
            var lastProgress = 0

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                //回调
                val currentProgress = (bytesWritten * 100 / contentLength).toInt()
                if (currentProgress > lastProgress) {
                    lastProgress = currentProgress
                    callback?.let {
                        val progress = Progress(lastProgress, bytesWritten, contentLength)
                        it.onProgress(progress)
                    }
                }
            }
        }
    }
}