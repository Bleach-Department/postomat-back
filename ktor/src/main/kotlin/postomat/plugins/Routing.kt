package postomat.plugins

import com.papsign.ktor.openapigen.route.apiRouting
import io.ktor.server.application.*
import postomat.routing.postomat
import postomat.routing.regions

fun Application.configureRouting() {
    apiRouting {
        regions()
        postomat()
    }
}

