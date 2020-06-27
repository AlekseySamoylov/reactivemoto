package com.alekseysamoylov.reactivemoto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@EnableR2dbcRepositories
@SpringBootApplication
class ReactivemotoApplication {
}

fun main(args: Array<String>) {
  runApplication<ReactivemotoApplication>(*args)
}
