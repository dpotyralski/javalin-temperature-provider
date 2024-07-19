package pl.dpotyralski

import io.mockk.mockk
import io.mockk.verify
import java.io.File
import kotlin.test.Test

class FileChangeObserverTest {
    private val averageTemperatureProvider = mockk<AverageTemperatureProvider>()

    private val tempFile = File.createTempFile("test", ".csv").toPath()
    private val fileChangeObserver = FileChangeObserver(tempFile, averageTemperatureProvider, 500)

    @Test
    fun `Should reload the data when the file changes`() {
        // given
        tempFile.toFile().writeText("test")

        // expect
        verify(exactly = 1, timeout = 1000) { averageTemperatureProvider.reloadData() }
    }
}
