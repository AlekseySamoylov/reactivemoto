package com.alekseysamoylov.reactivemoto.search

import com.alekseysamoylov.reactivemoto.Maker
import com.alekseysamoylov.reactivemoto.MakerRequest
import com.alekseysamoylov.reactivemoto.Motorcycle
import com.alekseysamoylov.reactivemoto.MotorcycleRequest
import com.alekseysamoylov.reactivemoto.repository.MotorcycleRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.SynchronousSink
import java.time.Duration


interface MotoSearchService {
  fun findAllMoto(): Flux<Motorcycle>
  fun findMoto(request: MotorcycleRequest): Flux<Motorcycle>
  fun findMaker(request: MakerRequest): Flux<Maker>
}

@Service
class DefaultMotoSearchService(
    private val motorcycleRepository: MotorcycleRepository
) : MotoSearchService {
  private val log = LoggerFactory.getLogger(this::class.java)

  override fun findAllMoto(): Flux<Motorcycle> {
    return motorcycleRepository.findAll()
        .delayElements(Duration.ofSeconds(1))
  }

  override fun findMoto(request: MotorcycleRequest): Flux<Motorcycle> {
    log.warn("Not implemented. The message to search {}", request)
    return motorcycleRepository.findAll()
        .delayElements(Duration.ofSeconds(1))
  }

  override fun findMaker(request: MakerRequest): Flux<Maker> {
    log.warn("Not implemented. The message to search {}", request.name)
    return Flux.generate { sink: SynchronousSink<Maker> ->
        sink.next(Maker(1, "BMW"))
    }.delayElements(Duration.ofSeconds(1))
  }
}



