package pl.dpotyralski

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import java.io.File
import java.nio.file.Path
import kotlin.io.path.name

private val logger = KotlinLogging.logger {}

class FileChangeObserver(
    private val temperatureCsvFile: Path,
    private val averageTemperatureProvider: AverageTemperatureProvider,
    private val fileLookUpIntervalMillis: Long,
) {
    init {

        val monitor = FileAlterationMonitor(fileLookUpIntervalMillis)
        val fileAlterationListenerAdaptor =
            object : FileAlterationListenerAdaptor() {
                override fun onFileChange(file: File) {
                    logger.info { "File ${temperatureCsvFile.name} has been modified" }
                    averageTemperatureProvider.reloadData()
                }
            }

        val observer =
            FileAlterationObserver(
                temperatureCsvFile.toFile().parentFile,
            ) { file ->
                file.name == temperatureCsvFile.name
            }
        observer.addListener(fileAlterationListenerAdaptor)
        monitor.addObserver(observer)
        monitor.start()
    }
}
