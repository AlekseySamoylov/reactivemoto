package com.alekseysamoylov.reactivemoto

import com.alekseysamoylov.reactivemoto.rdbmsrepository.TestMotorcycleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Tag("integration")
class RestComponentTest {
  private val log = LoggerFactory.getLogger(this::class.java)

  @Test
  fun shouldGetMotorcycleRest() {
    val webClient = WebClient.builder()
        .baseUrl("http://localhost:8080")
        .build()
    val motorcycles = mutableListOf<Motorcycle>()

    val countDownLatch = CountDownLatch(8)
    webClient.get().uri("/api/moto")
        .retrieve()
        .bodyToFlux(Motorcycle::class.java)
        .subscribe {
          countDownLatch.countDown()
          log.info("Moto {}", it)
          motorcycles.add(it)
        }


    webClient.get().uri("/api/moto/{name}", "Honda")
        .retrieve()
        .bodyToFlux(Motorcycle::class.java)
        .subscribe {
          countDownLatch.countDown()
          log.info("Moto by name {}", it)
        }
    countDownLatch.await(10, TimeUnit.SECONDS)
    assertThat(countDownLatch.count).isEqualTo(0)
    assertThat(motorcycles).containsExactly(TestMotorcycleRepository.aprilia,
        TestMotorcycleRepository.bmw, TestMotorcycleRepository.ducati, TestMotorcycleRepository.yamaha)
    log.info("moto {}", motorcycles)

  }
}
