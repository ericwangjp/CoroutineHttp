package com.common.httpnetwork

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.common.httpnetwork.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        initData()

//        lifecycleScope.launchWhenCreated {
//        }

    }

    private fun initData() {
        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        activityMainBinding.btnCommon.setOnClickListener {
            viewModel.searchSong("西游记").observe(this, {
                activityMainBinding.tvResponseBody.text = it?.toString()
            })
        }

        activityMainBinding.btnDownload.setOnClickListener {
            Log.e("开始下载", "==========================")
            val file = File(cacheDir, "zxapp.apk")
            viewModel.download(file).observe(this, {
                activityMainBinding.tvDownloadProgress.text =
                    "下载进度==>${it?.progress}\n文件路径：${file.absolutePath}"
            })
        }


    }

}