package com.alekseysamoylov.reactivemoto

import com.fasterxml.jackson.databind.type.SimpleType
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandler
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.reflect.Type
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit

class WebsocketComponentTest {
  private val log = LoggerFactory.getLogger(this::class.java)

  @Test
  fun shouldGetMotorcycle() {
    val uri: URI
    try {
      uri = URI("ws://localhost:8080/ws/motorcycle")
    } catch (e: URISyntaxException) {
      e.printStackTrace()
      return
    }

    val mWebSocketClient = object : WebSocketClient(uri) {
      override fun onOpen(serverHandshake: ServerHandshake) {
        log.info("Websocket >>>> Opened")
        this.send("Hello Moto")
      }

      override fun onMessage(s: String) {
        log.info("Message >>>> {}", s)
      }

      override fun onClose(i: Int, s: String, b: Boolean) {
        log.info("Websocket Closed {}", s)
      }

      override fun onError(e: Exception) {
        log.error("Websocket", e)
      }
    }
    mWebSocketClient.connect()

    TimeUnit.SECONDS.sleep(20)
    mWebSocketClient.close()

  }
}
