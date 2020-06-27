package com.alekseysamoylov.reactivemoto.elasticrepository

import com.alekseysamoylov.reactivemoto.SearchableMotorcycle
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux


interface MotorcycleElasticRepository : ReactiveCrudRepository<SearchableMotorcycle, Long> {
  fun findAllByName(name: String): Flux<SearchableMotorcycle>
}
