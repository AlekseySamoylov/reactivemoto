package com.alekseysamoylov.reactivemoto


data class Maker(
    val id: Long,
    val name: String
)

data class Motorcycle(
    val id: Long,
    private val maker: Maker,
    val name: String,
    val year: Int
)

data class MotorcycleRequest(val modelName: String = "", val makerName: String = "")
data class MakerRequest(val name: String = "")
