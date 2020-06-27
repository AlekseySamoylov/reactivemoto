package com.alekseysamoylov.reactivemoto.rdbmsrepository

import com.alekseysamoylov.reactivemoto.Maker
import org.springframework.data.repository.reactive.ReactiveCrudRepository


interface MakerRepository : ReactiveCrudRepository<Maker, Long> {
}
