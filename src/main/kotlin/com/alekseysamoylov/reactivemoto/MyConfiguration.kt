package com.alekseysamoylov.reactivemoto


interface MyConfiguration {
  fun getMotoCsvFilePath(): String
}

object DefaultMyConfiguration: MyConfiguration {
  private val motoCsvFilePath: String = System.getenv("MOTO_CSV_FILE_PATH")?: "./files/moto.csv"
  override fun getMotoCsvFilePath() = motoCsvFilePath
}

