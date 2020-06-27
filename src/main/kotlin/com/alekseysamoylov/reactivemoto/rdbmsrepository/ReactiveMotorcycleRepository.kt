package com.alekseysamoylov.reactivemoto.rdbmsrepository

import com.alekseysamoylov.reactivemoto.Motorcycle
import org.springframework.data.repository.reactive.ReactiveCrudRepository


interface ReactiveMotorcycleRepository: ReactiveCrudRepository<Motorcycle, Long>
