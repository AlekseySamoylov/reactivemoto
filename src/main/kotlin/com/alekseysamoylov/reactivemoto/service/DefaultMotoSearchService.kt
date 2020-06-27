package com.alekseysamoylov.reactivemoto.service

import com.alekseysamoylov.reactivemoto.Maker
import com.alekseysamoylov.reactivemoto.MakerRequest
import com.alekseysamoylov.reactivemoto.Motorcycle
import com.alekseysamoylov.reactivemoto.MotorcycleRequest
import com.alekseysamoylov.reactivemoto.repository.MakerRepository
import com.alekseysamoylov.reactivemoto.repository.ReactiveMotorcycleRepository
import com.alekseysamoylov.reactivemoto.util.CsvToMotorcycleMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import javax.annotation.PostConstruct


interface MotoSearchService {
  fun findAllMoto(): Flux<Motorcycle>
  fun findMoto(request: MotorcycleRequest): Flux<Motorcycle>
  fun findMaker(request: MakerRequest): Flux<Maker>
}

@Service
class DefaultMotoSearchService(
    private val reactiveMotorcycleRepository: ReactiveMotorcycleRepository,
    private val makerRepository: MakerRepository,
    private val csvToMotorcycleMapper: CsvToMotorcycleMapper
) : MotoSearchService {
  private val log = LoggerFactory.getLogger(this::class.java)

  @PostConstruct
  fun prepareData() {
    val firstMaker = makerRepository.findById(1).block()
    if (firstMaker == null) {
      log.info("Initialize data from csv")
      val motorcycles = csvToMotorcycleMapper.getAllMotorcycles()
      val makers = csvToMotorcycleMapper.getAllMakers()
      makerRepository.saveAll(makers).blockLast()
      reactiveMotorcycleRepository.saveAll(motorcycles).blockLast()
    } else {
      log.info("Data already exists")
    }
  }


  override fun findAllMoto(): Flux<Motorcycle> {
    return reactiveMotorcycleRepository.findAll()
        .delayElements(Duration.ofSeconds(1))
  }

  override fun findMoto(request: MotorcycleRequest): Flux<Motorcycle> {
    log.warn("Not implemented. The message to search {}", request)
    return reactiveMotorcycleRepository.findAll()
        .delayElements(Duration.ofSeconds(1))
  }

  override fun findMaker(request: MakerRequest): Flux<Maker> {
    log.warn("Not implemented. The message to search {}", request.name)
    return makerRepository.findAll().delayElements(Duration.ofSeconds(1))
  }
}

/*
var motorcycle: Motorcycle? = null
val countDownLatch = CountDownLatch(1)
reactiveMotorcycleRepository.findById(50).subscribe {
  motorcycle = it
  countDownLatch.countDown()
}
countDownLatch.await(5, TimeUnit.SECONDS)
motorcycle
 */


