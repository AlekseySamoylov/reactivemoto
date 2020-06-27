package com.alekseysamoylov.reactivemoto.util

import com.alekseysamoylov.reactivemoto.Maker
import com.alekseysamoylov.reactivemoto.Motorcycle
import com.alekseysamoylov.reactivemoto.MyConfiguration
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.FileReader


interface CsvToMotorcycleMapper {
  fun getAllMotorcycles(): List<Motorcycle>
}

class DefaultCsvToMotorcycleMapper(private val configuration: MyConfiguration) : CsvToMotorcycleMapper {
  private val log = LoggerFactory.getLogger(this::class.java)
  private val makersMap = mutableMapOf<String, Maker>()
  override fun getAllMotorcycles(): List<Motorcycle> {
    val motorcycles = mutableListOf<Motorcycle>()
    BufferedReader(FileReader(configuration.getMotoCsvFilePath())).use { br ->
      var row = br.readLine()
      while (row != null) {
        val data = row.split(",")
        row = br.readLine()
        if (data[0] == "ID") {
          continue
        }
        val maker = makersMap.computeIfAbsent(data[1]) { Maker(id = makersMap.keys.size.toLong() + 1, name = data[1]) }
        val motorcycle = Motorcycle(data[0].toLong(), maker, name = data[2], year = data[3].toInt())
        motorcycles.add(motorcycle)
      }
    }
    log.info("Found motorcycles={}", motorcycles.size)
    return motorcycles
  }
}
