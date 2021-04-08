package com.cxz.websocket

/**
 * @author chenxz
 * @date 2021/4/7
 * @desc 连接状态
 */
enum class WebSocketStatus {
    INIT,
    CONNECTING, // the initial state of each web socket.
    CONNECTED, // the web socket has been accepted by the remote peer
    CLOSING, // one of the peers on the web socket has initiated a graceful shutdown
    CLOSED, //  the web socket has transmitted all of its messages and has received all messages from the peer
    CANCELED // the web socket connection failed
}