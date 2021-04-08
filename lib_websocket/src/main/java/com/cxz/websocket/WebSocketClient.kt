package com.cxz.websocket

import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

/**
 * @author chenxz
 * @date 2021/4/7
 * @desc WebSocketClient
 */
class WebSocketClient private constructor() : WebSocketListener() {

    companion object {
        val instance: WebSocketClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            return@lazy WebSocketClient()
        }
    }

    private var webSocket: WebSocket? = null

    // 连接地址
    private var wsUrl = ""

    // 连接状态
    private var status: WebSocketStatus = WebSocketStatus.INIT

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .pingInterval(20, TimeUnit.SECONDS) // 设置PING帧发送间隔
            .build()
    }

    private var webSocketCallback: WebSocketCallback? = null

    fun setWebSocketCallback(webSocketCallback: WebSocketCallback?) {
        this.webSocketCallback = webSocketCallback
    }

    fun getStatus(): WebSocketStatus {
        return status
    }

    /**
     * 开始连接服务器
     */
    fun connectServer(url: String) {
        if (url.isEmpty()) {
            throw RuntimeException("url must not be empty!")
        }
        this.wsUrl = url
        // 构造Request对象
        val request = Request.Builder()
            .url(url)
            .build()
        webSocket = client.newWebSocket(request, this)
        status = WebSocketStatus.CONNECTING
    }

    /**
     * 重连服务器
     */
    fun reconnectServer() {
        if (webSocket != null) {
            webSocket = client.newWebSocket(webSocket!!.request(), this)
        } else {
            connectServer(wsUrl)
        }
    }

    /**
     * 发送数据
     */
    fun send(data: String) {
        // TODO
        webSocket?.send(data)
    }

    /**
     * 断开长连接
     */
    fun disconnectServer() {
        webSocket?.cancel()
        webSocket?.close(1001, null)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        this.status = WebSocketStatus.CONNECTED
        webSocketCallback?.onOpen()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        webSocketCallback?.onMessage(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        webSocketCallback?.onMessage(bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        this.status = WebSocketStatus.CLOSING
        webSocketCallback?.onClosing(code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        this.status = WebSocketStatus.CLOSED
        webSocketCallback?.onClosed(code, reason)
        // 重连
        reconnectServer()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        t.printStackTrace()
        this.status = WebSocketStatus.CANCELED
        webSocketCallback?.onFailure(t)
        // 重连
        reconnectServer()
    }

}