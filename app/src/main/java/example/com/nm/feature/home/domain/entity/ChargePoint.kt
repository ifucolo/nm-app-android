package example.com.nm.feature.home.domain.entity

class ChargePoint(
    val city: String,
    val conenctors: List<Conector>,
    val lat: Double,
    val lng: Double
)

class Conector(
    val id: String,
    val address: String,
    val connectorType: String,
    val power: Power
)

class Power(
    val current: String,
    val phase: String,
    val voltage: Int,
    val amperage: Int
)