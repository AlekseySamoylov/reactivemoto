package com.alekseysamoylov.reactivemoto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactivemotoApplication {
}

fun main(args: Array<String>) {
  runApplication<ReactivemotoApplication>(*args)
}
