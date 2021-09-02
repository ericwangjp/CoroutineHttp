package com.common.httpnetwork

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.common.httplib.model.BaseViewModel
import com.common.httplib.utils.HttpUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var viewModel = ViewModelProvider(this)[BaseViewModel::class.java]


        lifecycleScope.launchWhenCreated {
            viewModel.httpRequest {
                HttpUtil.apiService.searchSongs<String>("潮汐")
            }
//            Log.e("当前线程1", Thread.currentThread().name)  // main
//            val songs = HttpUtil.httpRequest {
//                Log.e("当前线程2", Thread.currentThread().name)  //  DefaultDispatcher-worker-1
//                HttpUtil.apiService.searchSongs<String>("潮汐")
//            }
//            Log.e("响应参数：", "==>$songs")
//            Log.e("当前线程3", Thread.currentThread().name)  // main


        }

    }

}