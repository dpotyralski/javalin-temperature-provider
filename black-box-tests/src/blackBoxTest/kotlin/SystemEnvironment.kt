import io.github.oshai.kotlinlogging.DelegatingKLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File

private val logger = KotlinLogging.logger {} as DelegatingKLogger<org.slf4j.Logger>

private const val TEMPERATURE_PROVIDER = "temperature-provider"

object SystemTestEnvironment {
    private val environment =
        ComposeContainer(File("src/blackBoxTest/resources/bb-docker-compose.yaml"))
            .apply {
                withLocalCompose(true)
                withLogConsumer(
                    TEMPERATURE_PROVIDER,
                    Slf4jLogConsumer(logger.underlyingLogger).withPrefix(TEMPERATURE_PROVIDER),
                )
                withExposedService(TEMPERATURE_PROVIDER, 8080, Wait.forHttp("/api/health").forStatusCode(200))
                withRemoveImages(ComposeContainer.RemoveImages.LOCAL)
            }

    private var environmentStarted = false

    fun start() {
        if (!environmentStarted) {
            environmentStarted = true
            environment.start()
            Runtime.getRuntime().addShutdownHook(Thread { environment.stop() })
        }
    }

    val serviceUrl: String by lazy {
        val host = environment.getServiceHost(TEMPERATURE_PROVIDER, 8080)
        val port = environment.getServicePort(TEMPERATURE_PROVIDER, 8080)
        "http://$host:$port/api/"
    }
}
