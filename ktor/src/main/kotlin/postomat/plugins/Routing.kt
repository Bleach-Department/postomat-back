package postomat.plugins

import io.github.dellisd.spatialk.geojson.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import me.plony.empty.Empty
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
    }
}
