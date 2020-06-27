package com.alekseysamoylov.reactivemoto.repository

import com.alekseysamoylov.reactivemoto.Maker
import com.alekseysamoylov.reactivemoto.Motorcycle
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux


interface MotorcycleRepository {
  fun findAll(): Flux<Motorcycle>
}

@Repository
class DefaultMotorcycleRepository: MotorcycleRepository {
  companion object {
    val aprilia = Motorcycle(1, Maker(1, "Aprilia"),"RSV 1000R Factory", 2009)
    val bmw = Motorcycle(2, Maker(2, "BMW"),"S 1000 RR Sport", 2017)
    val ducati = Motorcycle(3, Maker(3, "Ducati"),"Monster 1200 S ABS", 2017)
    val yamaha = Motorcycle(4, Maker(4, "Yamaha"),"YZF-R1", 2017)
  }

  private val motoMap = mutableMapOf(1L to aprilia, 2L to bmw, 3L to ducati, 4L to yamaha)
  override fun findAll(): Flux<Motorcycle> {
    return motoMap.values.stream().toFlux()
  }

}
