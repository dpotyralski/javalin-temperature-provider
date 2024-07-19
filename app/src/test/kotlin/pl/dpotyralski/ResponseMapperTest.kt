package pl.dpotyralski

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseMapperTest {
    @Test
    fun `map should correctly transform non-empty map to list of YearlyAverageTemperatureResponse`() {
        // given
        val inputMap =
            mapOf(
                2021 to BigDecimal("15.5"),
                2022 to BigDecimal("16.3"),
            )
        val expected =
            listOf(
                YearlyAverageTemperatureResponse(2021, BigDecimal("15.5")),
                YearlyAverageTemperatureResponse(2022, BigDecimal("16.3")),
            )

        // when
        val result = ResponseMapper.map(inputMap)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `map should return empty list for empty input map`() {
        // given
        val inputMap = emptyMap<Int, BigDecimal>()

        // when
        val result = ResponseMapper.map(inputMap)

        // then
        assertEquals(emptyList(), result)
    }
}
