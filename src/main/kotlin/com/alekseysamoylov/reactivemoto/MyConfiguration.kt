package com.alekseysamoylov.reactivemoto

import org.springframework.stereotype.Component


interface MyConfiguration {
  fun getMotoCsvFilePath(): String
}

@Component
object DefaultMyConfiguration: MyConfiguration {
  private val motoCsvFilePath: String = System.getenv("MOTO_CSV_FILE_PATH")?: "./files/moto.csv"
  override fun getMotoCsvFilePath() = motoCsvFilePath
}

