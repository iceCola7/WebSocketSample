package com.cxz.websocket

import okio.ByteString

/**
 * @author chenxz
 * @date 2021/4/7
 * @desc WebSocketSendManager
 */
class WebSocketSendManager {

    interface WebSocketSendChangeObservable {
        /**
         * 接收消息
         * @param data String
         */
        fun onMessage(data: String) {}

        /**
         * 接收消息
         * @param bytes ByteString
         */
        fun onMessage(bytes: ByteString) {}
    }

    companion object {

        private val listObservers: ArrayList<WebSocketSendChangeObservable> = ArrayList()

        val instance: WebSocketSendManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            return@lazy WebSocketSendManager()
        }
    }

    /**
     * 注册
     */
    fun registerWebSocketSendChangeObservable(observable: WebSocketSendChangeObservable?) {
        if (observable != null) {
            if (!listObservers.contains(observable)) {
                listObservers.add(observable)
            }
        }
    }

    /**
     * 取消注册
     */
    fun unregisterWebSocketSendChangeObservable(observable: WebSocketSendChangeObservable?) {
        if (observable != null) {
            listObservers.remove(observable)
        }
    }

    /**
     * 接收消息
     * @param data String
     */
    fun onMessage(data: String) {
        listObservers.forEach {
            it.onMessage(data)
        }
    }

    /**
     * 接收消息
     * @param bytes ByteString
     */
    fun onMessage(bytes: ByteString) {
        listObservers.forEach {
            it.onMessage(bytes)
        }
    }

}