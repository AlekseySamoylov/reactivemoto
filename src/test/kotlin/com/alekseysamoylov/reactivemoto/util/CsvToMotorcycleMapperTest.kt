package com.alekseysamoylov.reactivemoto.util

import com.alekseysamoylov.reactivemoto.Maker
import com.alekseysamoylov.reactivemoto.Motorcycle
import com.alekseysamoylov.reactivemoto.TestMyConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class CsvToMotorcycleMapperTest {

  @Test
  fun shouldParseMotoFromCsv() {
    // Given
    val expectedAprilia = Motorcycle(1, maker = Maker(1, name = "Aprilia"), name = "RSV 1000R Factory", year = 2009, makerId = 1)
    val expectedBmw = Motorcycle(2, maker = Maker(2, name = "BMW"), name = "S 1000 RR Sport", year = 2017, makerId = 2)
    val expectedDucati = Motorcycle(3, maker = Maker(3, name = "Ducati"), name = "Monster 1200 S ABS", year = 2017, makerId = 3)
    val expectedYamaha = Motorcycle(4, maker = Maker(4, name = "Yamaha"), name = "YZF-R1", year = 2017, makerId = 4)
    val parser = DefaultCsvToMotorcycleMapper(TestMyConfiguration)

    // When
    val result = parser.getAllMotorcycles()

    // Then
    assertThat(result).containsExactly(expectedAprilia, expectedBmw, expectedDucati, expectedYamaha)
  }
}
