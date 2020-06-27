package com.alekseysamoylov.reactivemoto.websocket

import com.alekseysamoylov.reactivemoto.MakerRequest
import com.alekseysamoylov.reactivemoto.MotorcycleRequest
import com.alekseysamoylov.reactivemoto.repository.DefaultMotorcycleRepository
import com.alekseysamoylov.reactivemoto.search.MotoSearchService
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import reactor.core.publisher.Flux
import reactor.core.publisher.SynchronousSink


@Configuration
class MotorcycleWebSocketConfiguration {

  @Bean
  fun simpleUrlHandlerMapping(motorcycleWebSocketHandler: WebSocketHandler, makerWebSocketHandler: WebSocketHandler): SimpleUrlHandlerMapping {
    return SimpleUrlHandlerMapping(mapOf(
        "/ws/motorcycle" to motorcycleWebSocketHandler,
        "/ws/maker" to makerWebSocketHandler
    ), 10)
  }

    val jsonObjectMapper = ObjectMapper()

  val eventFlux = Flux.generate { sink: SynchronousSink<String?> ->
    val event = DefaultMotorcycleRepository.aprilia
    try {
      sink.next(jsonObjectMapper.writeValueAsString(event))
    } catch (e: JsonProcessingException) {
      sink.error(e)
    }
  }

  @Bean
  fun motorcycleWebSocketHandler(motoSearchService: MotoSearchService): WebSocketHandler {
    return WebSocketHandler { session ->
      val receive = session.receive()
      val searchMotorcycleModelString = receive.map { it.payloadAsText }
      val requestFlux = searchMotorcycleModelString.map { MotorcycleRequest(it) }
      val motorcycleModelResponseFlux = requestFlux.flatMap { motoSearchService.findMoto(it) }
      val map = motorcycleModelResponseFlux.map { jsonObjectMapper.writeValueAsString(it) }
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
