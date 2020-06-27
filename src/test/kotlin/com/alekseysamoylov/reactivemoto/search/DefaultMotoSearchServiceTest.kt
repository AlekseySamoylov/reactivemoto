package com.alekseysamoylov.reactivemoto.search

import com.alekseysamoylov.reactivemoto.repository.TestMotorcycleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.streams.toList


internal class DefaultMotoSearchServiceTest {

  @Test
  fun shouldSearchMotorcycles() {
    // Given
    val service = DefaultMotoSearchService(TestMotorcycleRepository())

    // When
    val results = service.findAllMoto()

    // Then
    assertThat(results.toStream().toList()).containsExactly(TestMotorcycleRepository.aprilia,
        TestMotorcycleRepository.bmw, TestMotorcycleRepository.ducati, TestMotorcycleRepository.yamaha)
  }
}
