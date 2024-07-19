package pl.dpotyralski

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AverageTemperatureProviderTest {
    private val temperatureCsvFileReader = io.mockk.mockk<TemperatureCsvFileReader>()
    private val averageTemperatureProvider = AverageTemperatureProvider(temperatureCsvFileReader)

    @Test
    fun `Should get calculated average per city`() {
        // given
        io.mockk.every {
            temperatureCsvFileReader.get()
        } returns
            listOf(
                TemperatureRecord("Warsaw", LocalDateTime.of(2021, 1, 1, 0, 0), "6".toBigDecimal()),
                TemperatureRecord("Warsaw", LocalDateTime.of(2021, 1, 1, 0, 0), "2".toBigDecimal()),
                TemperatureRecord("Warsaw", LocalDateTime.of(2021, 1, 1, 0, 0), "4".toBigDecimal()),
                TemperatureRecord("Warsaw", LocalDateTime.of(2022, 1, 1, 0, 0), "15".toBigDecimal()),
                TemperatureRecord("Warsaw", LocalDateTime.of(2023, 1, 1, 0, 0), "20".toBigDecimal()),
            )

        // when
        averageTemperatureProvider.reloadData()

        // then
        assertEquals(3, averageTemperatureProvider.byCity("Warsaw").size)
        assertEquals("4".toBigDecimal(), averageTemperatureProvider.byCity("Warsaw")[2021])
    }

    @Test
    fun `Should get empty map in case there is no records`() {
        // given
        io.mockk.every {
            temperatureCsvFileReader.get()
        } returns
            emptyList()

        // when
        averageTemperatureProvider.reloadData()

        // then
        assertTrue(averageTemperatureProvider.byCity("Warsaw").isEmpty())
    }
}
