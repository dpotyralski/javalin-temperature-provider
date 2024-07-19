package pl.dpotyralski

import java.math.BigDecimal

object ResponseMapper {
    fun map(weatherRecords: Map<Int, BigDecimal>): List<YearlyAverageTemperatureResponse> =
        weatherRecords.map { (year, averageTemperature) ->
            YearlyAverageTemperatureResponse(year, averageTemperature)
        }
}

data class YearlyAverageTemperatureResponse(
    val year: Int,
    val averageTemperature: BigDecimal,
)
