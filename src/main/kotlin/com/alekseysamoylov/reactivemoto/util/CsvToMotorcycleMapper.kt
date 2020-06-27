package com.alekseysamoylov.reactivemoto.util

import com.alekseysamoylov.reactivemoto.Maker
import com.alekseysamoylov.reactivemoto.Motorcycle
import com.alekseysamoylov.reactivemoto.MyConfiguration
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.FileReader


interface CsvToMotorcycleMapper {
  fun getAllMotorcycles(): List<Motorcycle>
  fun getAllMakers(): List<Maker>
}

@Component
class DefaultCsvToMotorcycleMapper(private val configuration: MyConfiguration) : CsvToMotorcycleMapper {
  private val log = LoggerFactory.getLogger(this::class.java)
  private final val makersMap = mutableMapOf<String, Maker>()
  private final val motorcycles = mutableListOf<Motorcycle>()

  init {
    BufferedReader(FileReader(configuration.getMotoCsvFilePath())).use { br ->
      var row = br.readLine()
      while (row != null) {
        val data = row.split(",")
        row = br.readLine()
        if (data[0] == "ID") {
          continue
        }
        val maker = makersMap.computeIfAbsent(data[1]) { Maker(id = null /*makersMap.keys.size.toLong() + 1*/, name = data[1]) }
        val motorcycle = Motorcycle(/*data[0].toLong()*/null, /*maker,*/ name = data[2], year = data[3].toInt(), makerId = makersMap.keys.size.toLong() + 1)
        motorcycles.add(motorcycle)
      }
    }
    log.info("Found motorcycles={}", motorcycles.size)
  }

  override fun getAllMotorcycles(): List<Motorcycle> {
    return motorcycles.toList()
  }

  override fun getAllMakers(): List<Maker> {
    return makersMap.values.toList()
  }
}
