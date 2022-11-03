package postomat.plugins

import io.bkbn.kompendium.core.routes.redoc
import io.github.dellisd.spatialk.geojson.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import me.plony.empty.Empty
import me.plony.regions.Region
import me.plony.regions.regionOrNull
import postomat.routing.postomat
import postomat.routing.regions
import stubs.Stubs
import toGeo

fun Application.configureRouting() {
    routing {
        redoc("Postomat")
        regions()
        postomat()
    }
}

