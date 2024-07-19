package pl.dpotyralski

import com.google.inject.Guice
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.Stage
import com.google.inject.name.Names
import dev.misfitlabs.kotlinguice4.KotlinModule
import dev.misfitlabs.kotlinguice4.getInstance
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.EndpointGroup
import jakarta.inject.Named
import jakarta.inject.Provider
import java.nio.file.Path
import java.nio.file.Paths

private const val CSV_FILE_PATH = "CSV_FILE_PATH"
private const val FILE_LOOK_UP_INTERVAL_MILLIS = "FILE_LOOK_UP_INTERVAL_MILLIS"

private val logger = KotlinLogging.logger {}

class AppConfig {
    fun setup(): Javalin {
        val injector = Guice.createInjector(Stage.PRODUCTION, AppModule())
        val endpointGroup = injector.getInstance<EndpointGroup>()

        val averageTemperatureProvider = injector.getInstance<AverageTemperatureProvider>()
        averageTemperatureProvider.reloadData()

        return Javalin.create { config -> config.router.apiBuilder(endpointGroup) }
    }
}

class AppModule : KotlinModule() {
    override fun configure() {
        bind<Path>()
            .annotatedWith(Names.named(CSV_FILE_PATH))
            .toProvider<CsvFilePathProvider>()
        bind<Long>()
            .annotatedWith(Names.named(FILE_LOOK_UP_INTERVAL_MILLIS))
            .toProvider<FileLookUpIntervalMillisProvider>()

        bind<TemperatureResource>().`in`<Singleton>()
    }

    class CsvFilePathProvider : Provider<Path> {
        override fun get(): Path = Paths.get(System.getenv(CSV_FILE_PATH) ?: error("Please provide CSV_FILE_PATH environment variable"))
    }

    class FileLookUpIntervalMillisProvider : Provider<Long> {
        override fun get(): Long {
            val fileLookUpIntervalMillis = System.getenv(FILE_LOOK_UP_INTERVAL_MILLIS)
            if (fileLookUpIntervalMillis == null) {
                logger.info { "FILE_LOOK_UP_INTERVAL_MILLIS environment variable not set, using default value 5000" }
                return 5000L
            } else {
                return fileLookUpIntervalMillis.toLong()
            }
        }
    }

    @Provides
    @Singleton
    fun weatherCsvFileReader(
        @Named(CSV_FILE_PATH) fileName: Path,
    ): TemperatureCsvFileReader = TemperatureCsvFileReader(fileName)

    @Provides
    @Singleton
    fun fileChangeObserver(
        @Named(CSV_FILE_PATH) fileName: Path,
        @Named(FILE_LOOK_UP_INTERVAL_MILLIS) fileLookUpIntervalMillis: Long,
        averageTemperatureProvider: AverageTemperatureProvider,
    ): FileChangeObserver = FileChangeObserver(fileName, averageTemperatureProvider, fileLookUpIntervalMillis)

    @Provides
    @Singleton
    fun averageTemperatureProvider(temperatureCsvFileReader: TemperatureCsvFileReader): AverageTemperatureProvider =
        AverageTemperatureProvider(temperatureCsvFileReader)

    @Provides
    @Singleton
    fun javalinConfig(temperatureResource: TemperatureResource): EndpointGroup =
        EndpointGroup {
            path("/api/health") {
                get { ctx -> ctx.status(200) }
            }
            path("/api/v1/cities/") {
                path("{city}/yearly-averages") {
                    get(temperatureResource::get)
                }
            }
        }
}
