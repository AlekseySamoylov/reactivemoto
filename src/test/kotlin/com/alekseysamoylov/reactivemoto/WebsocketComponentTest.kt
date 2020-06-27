package com.alekseysamoylov.reactivemoto

import com.alekseysamoylov.reactivemoto.rdbmsrepository.TestMotorcycleRepository
import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Tag("integration")
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

    val countDownLatch = CountDownLatch(4)
    val jsonObjectMapper = Gson()
    val motorcycles = mutableListOf<Motorcycle>()
    val mWebSocketClient = object : WebSocketClient(uri) {
      override fun onOpen(serverHandshake: ServerHandshake) {
        log.info("Websocket >>>> Opened")
        this.send("Hello Moto")
      }

      override fun onMessage(s: String) {
        log.info("Message >>>> {}", s)
        val motorcycle = jsonObjectMapper.fromJson<Motorcycle>(s, Motorcycle::class.java)
        motorcycles.add(motorcycle)
        countDownLatch.countDown()
      }

      override fun onClose(i: Int, s: String, b: Boolean) {
        log.info("Websocket Closed {}", s)
      }

      override fun onError(e: Exception) {
        log.error("Websocket", e)
      }
    }
    mWebSocketClient.connect()

    countDownLatch.await(15, TimeUnit.SECONDS)
    assertThat(countDownLatch.count).isEqualTo(0)
    assertThat(motorcycles).containsExactly(TestMotorcycleRepository.aprilia,
        TestMotorcycleRepository.bmw, TestMotorcycleRepository.ducati, TestMotorcycleRepository.yamaha)
    log.info("moto {}", motorcycles)
    mWebSocketClient.close()

  }
}
