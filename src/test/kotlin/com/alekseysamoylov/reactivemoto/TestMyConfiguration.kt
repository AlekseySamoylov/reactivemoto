package com.alekseysamoylov.reactivemoto





object TestMyConfiguration: MyConfiguration {
  override fun getMotoCsvFilePath() = "./files/motoTest.csv"
}
