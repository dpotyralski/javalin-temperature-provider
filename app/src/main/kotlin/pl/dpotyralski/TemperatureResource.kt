package pl.dpotyralski

import io.javalin.http.Context
import jakarta.inject.Inject

class TemperatureResource
    @Inject
    constructor(
        private val temperatureProvider: AverageTemperatureProvider,
    ) {
        fun get(context: Context): Context {
            val weatherRecords = temperatureProvider.byCity(context.pathParam("city"))
            return context.json(ResponseMapper.map(weatherRecords))
        }
    }
