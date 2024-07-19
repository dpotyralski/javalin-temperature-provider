package pl.dpotyralski

import io.github.oshai.kotlinlogging.KotlinLogging
import java.math.BigDecimal
import kotlin.time.measureTimedValue

private val logger = KotlinLogging.logger {}

class AverageTemperatureProvider(
    private val temperatureCsvFileReader: TemperatureCsvFileReader,
) {
    private val map = mutableMapOf<String, Map<Int, BigDecimal>>()

    fun byCity(city: String): Map<Int, BigDecimal> = map.getOrDefault(city, emptyMap())

    fun reloadData() {
        val weatherRecords = getWeatherRecords()
        val transformed = createAverageTemperatureDistributionByCityAsKey(weatherRecords)
        transformed.forEach { (city, yearlyAverages) ->
            map[city] = yearlyAverages
        }
    }

    private fun createAverageTemperatureDistributionByCityAsKey(
        weatherRecords: List<TemperatureRecord>,
    ): Map<String, Map<Int, BigDecimal>> {
        val (transformed, transformationDuration) =
            measureTimedValue {
                weatherRecords.groupBy { it.city }.mapValues { listEntry ->
                    listEntry.value.groupBy { it.date.year }.mapValues { valuesPerCity ->
                        val listOfTemperatures = valuesPerCity.value.map { it.temperature }
                        listOfTemperatures.reduce { acc, bigDecimal -> acc + bigDecimal } / listOfTemperatures.size.toBigDecimal()
                    }
                }
            }
        logger.info { "Transformation took $transformationDuration" }
        return transformed
    }

    private fun getWeatherRecords(): List<TemperatureRecord> {
        val (weatherRecords, duration) =
            measureTimedValue {
                temperatureCsvFileReader.get()
            }
        logger.info { "Reading took $duration" }
        return weatherRecords
    }
}
