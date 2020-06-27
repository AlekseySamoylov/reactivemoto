package com.alekseysamoylov.reactivemoto.repository

import com.alekseysamoylov.reactivemoto.Motorcycle
import org.springframework.data.repository.reactive.ReactiveCrudRepository


interface ReactiveMotorcycleRepository: ReactiveCrudRepository<Motorcycle, Long>
