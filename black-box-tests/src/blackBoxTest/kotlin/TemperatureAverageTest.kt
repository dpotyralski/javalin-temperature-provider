import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TemperatureAverageTest {
    @BeforeAll
    fun setupEnv() {
        SystemTestEnvironment.start()
    }

    @ParameterizedTest
    @ValueSource(strings = ["Warszawa", "Kraków", "Łódź"])
    fun `should be able to successfully fetch yearly average temperature per city`(city: String) {
        given()
            .baseUri(SystemTestEnvironment.serviceUrl)
            .`when`()
            .get("/v1/cities/$city/yearly-averages")
            .then()
            .statusCode(200)
            .body("$.size()", greaterThan(0))
    }

    @ParameterizedTest
    @ArgumentsSource(CityTemperatureProvider::class)
    fun `should get expected values per city and year`(cityTemperature: CityTemperature) {
        given()
            .baseUri(SystemTestEnvironment.serviceUrl)
            .`when`()
            .get("/v1/cities/${cityTemperature.city}/yearly-averages")
            .then()
            .statusCode(200)
            .body(
                "find { it.year == ${cityTemperature.year} }.averageTemperature",
                equalTo(cityTemperature.expectedAverageTemperature),
            )
    }
}

class CityTemperatureProvider : ArgumentsProvider {
    override fun provideArguments(p0: ExtensionContext?): Stream<out Arguments> =
        Stream.of(
            Arguments.of(CityTemperature("Warszawa", 2018, -20.77F)),
            Arguments.of(CityTemperature("Warszawa", 2019, 16.1F)),
            Arguments.of(CityTemperature("Kraków", 2018, 6.82F)),
            Arguments.of(CityTemperature("Kraków", 2021, 22.94F)),
        )
}

data class CityTemperature(
    val city: String,
    val year: Int,
    val expectedAverageTemperature: Float,
)
