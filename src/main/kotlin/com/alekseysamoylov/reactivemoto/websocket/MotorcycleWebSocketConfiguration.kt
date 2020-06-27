package com.alekseysamoylov.reactivemoto.websocket

import com.alekseysamoylov.reactivemoto.MakerRequest
import com.alekseysamoylov.reactivemoto.MotorcycleRequest
import com.alekseysamoylov.reactivemoto.service.MotoSearchService
import com.google.gson.Gson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter


@Configuration
class MotorcycleWebSocketConfiguration {

  @Bean
  fun simpleUrlHandlerMapping(motorcycleWebSocketHandler: WebSocketHandler, makerWebSocketHandler: WebSocketHandler): SimpleUrlHandlerMapping {
    return SimpleUrlHandlerMapping(mapOf(
        "/ws/motorcycle" to motorcycleWebSocketHandler,
        "/ws/maker" to makerWebSocketHandler
    ), 10)
  }

  val jsonObjectMapper = Gson()

  @Bean
  fun motorcycleWebSocketHandler(motoSearchService: MotoSearchService): WebSocketHandler {
    return WebSocketHandler { session ->
      val receive = session.receive()
      val searchMotorcycleModelString = receive.map { it.payloadAsText }
      val requestFlux = searchMotorcycleModelString.map { MotorcycleRequest(it) }
      val motorcycleModelResponseFlux = requestFlux.flatMap { motoSearchService.findMoto(it) }
      val map = motorcycleModelResponseFlux.map { jsonObjectMapper.toJson(it) }
      val webSocketMessageFlux = map.map { session.textMessage(it) }
      session.send(webSocketMessageFlux)
    }
  }

  @Bean
  fun makerWebSocketHandler(motoSearchService: MotoSearchService): WebSocketHandler {
    return WebSocketHandler { session ->
      val receive = session.receive()
      val searchMotorcycleModelString = receive.map { it.payloadAsText }
      val requestFlux = searchMotorcycleModelString.map { MakerRequest(it) }
      val motorcycleModelResponseFlux = requestFlux.flatMap { motoSearchService.findMaker(it) }
      val map = motorcycleModelResponseFlux.map { it.name }
      val webSocketMessageFlux = map.map { session.textMessage(it) }
      session.send(webSocketMessageFlux)
    }
  }


  @Bean
  fun webSocketHandlerAdapter(): WebSocketHandlerAdapter {
    return WebSocketHandlerAdapter()
  }
}
