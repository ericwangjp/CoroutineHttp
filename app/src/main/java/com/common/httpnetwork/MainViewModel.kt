package com.common.httpnetwork

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.httplib.base.BaseViewModel
import com.common.httplib.model.Progress
import com.common.httplib.model.ResultResp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: MainViewModel
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 9:42 下午
 *
 */
class MainViewModel : BaseViewModel() {

    private val repository by lazy {
        MainRepository()
    }

    private val songData by lazy {
        MutableLiveData<List<ResultResp>>()
    }

    fun searchSong(key: String): LiveData<List<ResultResp>> {

        request {
            val result = repository.searchSongs(key)
            songData.value = result?.result
        }
        return songData

    }

    private val downloadProgress by lazy {
        MutableLiveData<Progress>()
    }

    fun download(file: File): LiveData<Progress> {
        request {
            val result = repository.download(this) {
                downloadProgress.value = it
            }
            if (result != null && result.isSuccessful) {
                result.body()?.apply {
                    //            写入数据
                    Log.e("写入数据，当前线程2", Thread.currentThread().name)  // main 线程
                    withContext(Dispatchers.IO) {
                        Log.e("写入数据，当前线程3", Thread.currentThread().name) // 子线程
                        var os: OutputStream? = null
                        //获取下载输入流
                        val `is`: InputStream = byteStream()
                        var curLength: Long = 0
                        try {
                            //输出流
                            os = FileOutputStream(file)
                            var len: Int
                            val buff = ByteArray(1024)
                            while (`is`.read(buff).also { len = it } != -1) {
                                os.write(buff, 0, len)
                                curLength += len.toLong()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e("写入文件失败", "==>${e.message}")
                        } finally {
                            try {
                                os?.close() //关闭输出流
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            try {
                                `is`.close() //关闭输入流
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
        return downloadProgress
    }
}