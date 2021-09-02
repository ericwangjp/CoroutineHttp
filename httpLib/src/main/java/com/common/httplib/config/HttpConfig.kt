package com.common.httplib.config

/**
 *
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: HttpConfig
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2021/9/1 2:04 下午
 *
 */
object HttpConfig {
    //    ----------------------------------网络请求配置---------------------------------
    /**
     * 连接超时时间
     */
    const val CONNECT_TIME_OUT: Long = 15

    /**
     * 读取超时时间
     */
    const val READ_TIME_OUT: Long = 15

    /**
     * 写入超时时间
     */
    const val WRITE_TIME_OUT: Long = 15

    /**
     * 缓存内存最大值
     */
    const val MAX_CACHE_SIZE = (10 * 1024 * 1024).toLong()

    /**
     * 有网络时缓存有效期
     */
    const val MAX_CACHE_TIME_WITH_NET = 60

    /**
     * 无网络时缓存有效期
     */
    const val MAX_CACHE_TIME_WITHOUT_NET = 60 * 60 * 24

    /**
     * 请求为json类型
     */
    const val MEDIA_TYPE_JSON = "application/json;charset=utf-8"

    /**
     * 请求为二进制流类型
     */
    const val MEDIA_TYPE_STREAM = "application/otcet-stream"

    /**
     * 请求为文件类型
     */
    const val MEDIA_TYPE_FILE = "multipart/form-data"

    /**
     * 请求为文本类型
     */
    const val MEDIA_TYPE_TEXT = "text/plain"

    /**
     * 请求为图片类型
     */
    const val MEDIA_TYPE_IMAGE = "image/*"

    /**
     * 编码方式 utf-8
     */
    const val ENCODE_TYPE_UTF8 = "UTF-8"

    //------------------------------------server返回状态码----------------------------------
    /**
     * 成功状态码
     */
    const val RETURN_CODE_SUCCESS = "QB00000"

    /**
     * 系统异常
     */
    const val SYSTEM_ERROR = "QB10001"

    /**
     * 未登录
     */
    const val NOT_LOG_IN = "DJ10005"

    /**
     * 登录超时
     */
    const val LOG_TIME_OUT = "DJ80000"

    //   --------------------------------网络状态码-------------------------------------
    const val CODE_NET_ERROR = "4000"
    const val CODE_TIMEOUT = "4080"
    const val CODE_JSON_PARSE_ERROR = "4010"
    const val CODE_SERVER_ERROR = "5000"



    //    ----------------------------------接口api地址--------------------------------
    /**
     * 基础api域名
     */
//    val HTTP_BASE_URL: String = HttpEnvConfig.getHttpDomain()
    val HTTP_BASE_URL: String = "https://autumnfish.cn/"
}