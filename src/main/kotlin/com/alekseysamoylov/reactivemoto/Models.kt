package com.alekseysamoylov.reactivemoto

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Maker(
    @Id
    val id: Long? = null,
    val name: String = ""
)

@Table
data class Motorcycle(
    @Id
    val id: Long? = null,
//    @Transient
//    var maker: Maker? = null,
    val name: String = "",
    val year: Int = 0,
    var makerId: Long? = null
)

data class MotorcycleRequest(val modelName: String = "", val makerName: String = "")
data class MakerRequest(val name: String = "")
