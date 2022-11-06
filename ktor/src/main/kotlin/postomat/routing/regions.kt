package postomat.routing

import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.route.EndpointInfo
import com.papsign.ktor.openapigen.route.StatusCode
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Position
import io.github.reactivecircus.cache4k.Cache
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.json.JsonPrimitive
import me.plony.empty.Empty
import me.plony.regions.Region
import me.plony.regions.regionOrNull
import stubs.Stubs

val regionCache = Cache.Builder()
    .build<Int, FeatureCollection>()

fun NormalOpenAPIRoute.regions() {
    route("/geo") {
        get<Unit, List<RegionDTO>> {
            val regions = Stubs.region.getRegions(Empty.getDefaultInstance())
                .map { it.toDTO() }
                .toList()
            respond(regions)
        }

        route("ao.json") {
            get<Unit, String>(EndpointInfo(
                "Returns GeoJson of regions"
            )) {
                val geoJson = regionCache.get(1) {
                    val geo = Stubs.region
                        .getRegionsGeoJson(Empty.getDefaultInstance())

                    FeatureCollection(
                        geo.featuresList
                            .map {
                                it.toGeo()
                            }
                    )
                }
                respond(geoJson.json())
            }
        }
        route("/geo/contains") {
            get<Point, RegionDTO>(
                StatusCode(HttpStatusCode.OK),
                StatusCode(HttpStatusCode.NotFound)
            ) { point ->
                val region = Stubs.region
                    .getRegionContaining(me.plony.geo.point {
                        lat = point.lat
                        long = point.long
                    }).regionOrNull
                region?.toDTO()?.let {
                    respond(it)
                } ?: pipeline.call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

private fun Region.toDTO() = RegionDTO(id, name, if (hasParentId()) parentId else null)

@Response("Объект региона")
data class RegionDTO(
    val id: Long,
    val name: String,
    val parentId: Long?
)


fun me.plony.geo.Feature.toGeo() =
    Feature(
        MultiPolygon(
            geometry.polygonsList.map { it.toGeo() }
        ),
        propertiesList.associate {
            it.name to JsonPrimitive(it.value)
        }
    )

fun me.plony.geo.Polygon.toGeo() = ringsList.map {
    it.pointsList.map {
        it.toGeo()
    }
}

fun me.plony.geo.Point.toGeo() = Position(long, lat)
