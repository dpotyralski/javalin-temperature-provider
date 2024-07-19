package pl.dpotyralski

import java.math.BigDecimal
import java.time.LocalDateTime

data class TemperatureRecord(
    val city: String,
    val date: LocalDateTime,
    val temperature: BigDecimal,
)
