package postomat.routing

import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.content.type.binary.BinaryContentTypeParser.respond
import com.papsign.ktor.openapigen.route.EndpointInfo
import com.papsign.ktor.openapigen.route.StatusCode
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Point
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import me.plony.empty.Empty
import me.plony.regions.Region
import me.plony.regions.regionOrNull
import stubs.Stubs
import toGeo

fun NormalOpenAPIRoute.regions() {
    route("/geo") {
        route("ao.json") {
            get<Unit, String>(EndpointInfo(
                "Returns GeoJson of regions"
            )) {
                val geo = Stubs.region
                    .getRegionsGeoJson(Empty.getDefaultInstance())

                val geoJson = FeatureCollection(
                    geo.featuresList
                        .map {
                            it.toGeo()
                        }
                )
                respond(geoJson.json())
            }
        }
        route("/geo/contains") {
            get<postomat.routing.Point, RegionDTO>(
                StatusCode(HttpStatusCode.NotFound)
            ) { point ->
                val region = Stubs.region
                    .getRegionContaining(me.plony.geo.point {
                        lat = point.lat
                        long = point.long
                    }).regionOrNull
                region?.toMap()?.let {
                    respond(it)
                } ?: pipeline.call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

private fun Region.toMap() = RegionDTO(id, name)

@Response("Region")
data class RegionDTO(
    val id: Long,
    val name: String
)
