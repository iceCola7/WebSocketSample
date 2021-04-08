package com.cxz.websocket.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cxz.websocket.WebSocketCallback
import com.cxz.websocket.WebSocketClient

class MainActivity : AppCompatActivity(), WebSocketCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WebSocketClient.instance.setWebSocketCallback(this)
    }

    override fun onMessage(data: String) {
        super.onMessage(data)
    }

}