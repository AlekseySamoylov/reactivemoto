package com.alekseysamoylov.reactivemoto.rest

import com.alekseysamoylov.reactivemoto.Motorcycle
import com.alekseysamoylov.reactivemoto.MotorcycleRequest
import com.alekseysamoylov.reactivemoto.service.MotoSearchService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class RestConfiguration {

  @Bean
  fun routes(motoSearchService: MotoSearchService): RouterFunction<ServerResponse> {
    return route()
        .GET("/api/moto") {
          ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(
              motoSearchService.findAllMoto(), Motorcycle::class.java
          )
        }
        .GET("/api/moto/{name}") {
          ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(
              motoSearchService.findMoto(MotorcycleRequest(modelName = it.pathVariable("name"))), Motorcycle::class.java
          )
        }
        .GET("/api/searchmoto/{name}") {
          ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(
              motoSearchService.elasticFindMotoByName(it.pathVariable("name")), Motorcycle::class.java
          )
        }
        .build()
  }
}
