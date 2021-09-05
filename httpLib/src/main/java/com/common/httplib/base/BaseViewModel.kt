package com.common.httplib.base

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: BaseViewModel
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/2 7:10 下午
 *
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {
    fun request(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Exception) {
            Log.e("BaseViewModel出错了", "==>${e.stackTraceToString()}")
        }
    }

}