package pl.dpotyralski

import java.math.BigDecimal
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ofPattern

private const val EXTENDED_ISO_8601 = "yyyy-MM-dd HH:mm:ss.SSS"
private const val CSV_FILE_DELIMITER = ";"

class TemperatureCsvFileReader(
    private val temperatureCsvFile: Path,
) {
    fun get(): List<TemperatureRecord> {
        temperatureCsvFile.toFile().bufferedReader().use { reader ->
            return reader
                .lines()
                .map(::createWeatherRecord)
                .toList()
        }
    }

    private fun createWeatherRecord(line: String): TemperatureRecord {
        val parts = line.split(CSV_FILE_DELIMITER)
        return TemperatureRecord(
            city = parts[0],
            date =
                LocalDateTime.parse(
                    parts[1],
                    ofPattern(EXTENDED_ISO_8601),
                ),
            temperature = BigDecimal(parts[2]),
        )
    }
}
