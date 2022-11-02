package postomat.plugins

import io.github.dellisd.spatialk.geojson.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import me.plony.empty.Empty
import me.plony.regions.Region
import me.plony.regions.regionOrNull
import stubs.Stubs
import toGeo

fun Application.configureRouting() {
    routing {
        get("/geo/ao.json") {
            val geo = Stubs.region
                .getRegionsGeoJson(Empty.getDefaultInstance())

            val geoJson = FeatureCollection(
                geo.featuresList
                    .map {
                        it.toGeo()
                    }
            ).json()
            call.respond(geoJson)
        }
        get("/geo/contains") {
            val pointString = call.parameters["point"] ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val point = Point.fromJson(pointString)
            val region = Stubs.region
                .getRegionContaining(me.plony.geo.point {
                    lat = point.coordinates.latitude
                    long = point.coordinates.longitude
                }).regionOrNull
            region?.toMap()?.let {
                call.respond(
                    HttpStatusCode.OK,
                    it
                )
            } ?: call.respond(HttpStatusCode.NotFound)
        }
    }
}

private fun Region.toMap() = RegionDTO(id, name)

@Serializable
data class RegionDTO(
    val id: Long,
    val name: String
)
