package com.alekseysamoylov.reactivemoto

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
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

@Document(indexName = "sample", type = "motorcycle")
data class SearchableMotorcycle(
    @Id
    var id: String? = "",
    var maker: String = "",
    val name: String = "",
    val year: Int = 0
)

data class MotorcycleRequest(val modelName: String = "", val makerName: String = "")
data class MakerRequest(val name: String = "")
