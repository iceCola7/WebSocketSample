package com.cxz.websocket

import okio.ByteString

/**
 * @author chenxz
 * @date 2021/4/7
 * @desc
 */
interface WebSocketCallback {

    fun onOpen() {}

    fun onMessage(data: String) {}

    fun onMessage(data: ByteString) {}

    fun onClosing(code: Int, reason: String) {}

    fun onClosed(code: Int, reason: String) {}

    fun onFailure(t: Throwable) {}
}