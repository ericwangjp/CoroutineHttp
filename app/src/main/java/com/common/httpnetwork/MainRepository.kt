package com.common.httpnetwork

import android.util.Log
import com.common.httplib.base.BaseRepository
import com.common.httplib.base.BaseResponse
import com.common.httplib.config.HttpConfig
import com.common.httplib.model.Progress
import com.common.httplib.model.ResultResp
import com.common.httplib.utils.HttpUtil
import kotlinx.coroutines.CoroutineScope
import okhttp3.ResponseBody
import retrofit2.Response

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: MainRepository
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 9:43 下午
 *
 */
class MainRepository : BaseRepository() {
    suspend fun searchSongs(key: String): BaseResponse<List<ResultResp>>? = httpRequest {
        HttpUtil.apiService.searchSongs(key)
    }


    suspend fun download(
        coroutine: CoroutineScope? = null,
        progress: (suspend (Progress) -> Unit)? = null
    ): Response<ResponseBody>? =
        httpRequest {
            Log.e("MainRepository", "==>${Thread.currentThread().name}")  // 子线程
            HttpUtil.getDownloadService(coroutine, progress).downloadFile(HttpConfig.DOWNLOAD_PATH)
        }

//    fun download(
//        context: Context,
//        coroutine: CoroutineScope? = null,
//        progress: (suspend (Progress) -> Unit)? = null
//    ) = flow {
//        val response = HttpUtil.getDownloadService(coroutine, progress).downloadFile(
//            HttpUtil.convertToRequestBody(json),
//            HttpUtil.convertToRequestBody(json)
//        )
//
//        response.body()?.let { body ->
//            val length = body.contentLength()
//            val contentType = body.contentType().toString()
//            val ios = body.byteStream()
//            val info = try {
//                downloadBuildToOutputStream(context, contentType)
//            } catch (e: Exception) {
//                emit(DownloadStatus.DownloadError(e))
//                DownloadInfo(null)
//                return@flow
//            }
//            val ops = info.ops
//            if (ops == null) {
//                emit(DownloadStatus.DownloadError(RuntimeException("下载出错")))
//                return@flow
//            }
//            //下载的长度
//            var currentLength: Int = 0
//            //写入文件
//            val bufferSize = 1024 * 8
//            val buffer = ByteArray(bufferSize)
//            val bufferedInputStream = BufferedInputStream(ios, bufferSize)
//            var readLength: Int = 0
//            while (bufferedInputStream.read(buffer, 0, bufferSize)
//                    .also { readLength = it } != -1
//            ) {
//                ops.write(buffer, 0, readLength)
//                currentLength += readLength
//                emit(
//                    DownloadStatus.DownloadProcess(
//                        currentLength.toLong(),
//                        length,
//                        currentLength.toFloat() / length.toFloat()
//                    )
//                )
//            }
//            bufferedInputStream.close()
//            ops.close()
//            ios.close()
//            if (info.uri != null)
//                emit(DownloadStatus.DownloadSuccess(info.uri))
//            else emit(DownloadStatus.DownloadSuccess(Uri.fromFile(info.file)))
//
//        } ?: kotlin.run {
//            emit(DownloadStatus.DownloadError(RuntimeException("下载出错")))
//        }
//    }.flowOn(Dispatchers.IO)
//
//    private fun downloadBuildToOutputStream(context: Context, contentType: String): DownloadInfo {
//        val uri = build.getUri(contentType)
//        if (build.getDowloadFile() != null) {
//            val file = build.getDowloadFile()!!
//            return DownloadInfo(FileOutputStream(file), file)
//        } else if (uri != null) {
//            return DownloadInfo(context.contentResolver.openOutputStream(uri), uri = uri)
//        } else {
//            val name = build.getFileName()
//            val fileName = if (!name.isNullOrBlank()) name else "${System.currentTimeMillis()}.${
//                MimeTypeMap.getSingleton().getExtensionFromMimeType(contentType)
//            }"
//            val file = File("${context.filesDir}", fileName)
//            return DownloadInfo(FileOutputStream(file), file)
//        }
//    }
//
//    private class DownloadInfo(val ops: OutputStream?, val file: File? = null, val uri: Uri? = null)
}