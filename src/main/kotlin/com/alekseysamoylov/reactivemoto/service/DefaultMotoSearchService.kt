package com.alekseysamoylov.reactivemoto.service

import com.alekseysamoylov.reactivemoto.*
import com.alekseysamoylov.reactivemoto.elasticrepository.MotorcycleElasticRepository
import com.alekseysamoylov.reactivemoto.rdbmsrepository.MakerRepository
import com.alekseysamoylov.reactivemoto.rdbmsrepository.ReactiveMotorcycleRepository
import com.alekseysamoylov.reactivemoto.util.CsvToMotorcycleMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import javax.annotation.PostConstruct


interface MotoSearchService {
  fun elasticFindMotoByName(name: String): Flux<SearchableMotorcycle>
  fun findAllMoto(): Flux<Motorcycle>
  fun findMoto(request: MotorcycleRequest): Flux<Motorcycle>
  fun findMaker(request: MakerRequest): Flux<Maker>
}

@Service
class DefaultMotoSearchService(
    private val reactiveMotorcycleRepository: ReactiveMotorcycleRepository,
    private val makerRepository: MakerRepository,
    private val csvToMotorcycleMapper: CsvToMotorcycleMapper,
    private val elasticRepository: MotorcycleElasticRepository
) : MotoSearchService {
  private val log = LoggerFactory.getLogger(this::class.java)

  @PostConstruct
  fun prepareData() {
    log.info("Check RDBMS data")
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
    log.info("Check Elasticsearch data")
    val existsMotorcycle = elasticRepository.findAllByName("S 1000 RR").blockFirst()
    if (existsMotorcycle == null) {
      log.info("Adding Elasticsearch data")
      val searchableMotorcycles = mutableListOf<SearchableMotorcycle>()
      val makers = csvToMotorcycleMapper.getAllMakers()
      csvToMotorcycleMapper.getAllMotorcycles().forEach { motorcycle ->
        val searchableMotorcycle = SearchableMotorcycle("", makers[(motorcycle.makerId!! - 2).toInt()].name, motorcycle.name, motorcycle.year)
        searchableMotorcycles.add(searchableMotorcycle)
      }
      elasticRepository.saveAll(searchableMotorcycles).blockLast()
      log.info("Elasticsearch data added")
    } else {
      log.info("Elasticsearch data already exists")
    }
  }

  override fun elasticFindMotoByName(name: String): Flux<SearchableMotorcycle> {
    return elasticRepository.findAllByName(name)
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


/*
var motorcycle: SearchableMotorcycle? = null
val countDownLatch = CountDownLatch(1)
elasticRepository.findAllByName("100V").subscribe {
motorcycle = it
  countDownLatch.countDown()
}
countDownLatch.await(5, TimeUnit.SECONDS)
motorcycle
 */
