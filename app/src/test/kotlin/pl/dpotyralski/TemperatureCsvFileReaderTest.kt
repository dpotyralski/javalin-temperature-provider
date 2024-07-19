package pl.dpotyralski

import java.nio.file.Path
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertContentEquals

class TemperatureCsvFileReaderTest {
    private val temperatureCsvFileReader = TemperatureCsvFileReader(getTestResource("text_temperature_data_file.csv"))

    @Test
    fun `Should successfully read and parse provided temperature data to list`() {
        // when
        val temperatureRecords = temperatureCsvFileReader.get()

        // then
        assert(temperatureRecords.size == 4)
    }

    @Test
    fun `Should successfully parse records from csv file`() {
        // given
        val expected = provideExpectedTemperatureRecords()

        // when
        val temperatureRecords = temperatureCsvFileReader.get()

        // then
        assertContentEquals(expected, temperatureRecords)
    }

    private fun getTestResource(fileName: String): Path =
        Path.of(
            this.javaClass
                .getResource("/$fileName")
                ?.path ?: error("Test resource not found"),
        )

    private fun provideExpectedTemperatureRecords() =
        listOf(
            TemperatureRecord(
                "Warszawa",
                LocalDateTime.of(2018, 10, 1, 11, 16, 21, 279_000_000),
                "20.04".toBigDecimal(),
            ),
            TemperatureRecord(
                "Warszawa",
                LocalDateTime.of(2019, 10, 2, 13, 59, 18, 193_000_000),
                "33.58".toBigDecimal(),
            ),
            TemperatureRecord(
                "Kraków",
                LocalDateTime.of(2021, 11, 22, 13, 46, 22, 179_000_000),
                "33.52".toBigDecimal(),
            ),
            TemperatureRecord(
                "Kraków",
                LocalDateTime.of(2022, 11, 24, 8, 10, 19, 885_000_000),
                "6.48".toBigDecimal(),
            ),
        )
}
