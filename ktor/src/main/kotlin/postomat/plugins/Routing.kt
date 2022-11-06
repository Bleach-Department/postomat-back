package postomat.plugins

import com.papsign.ktor.openapigen.route.apiRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import postomat.routing.postomat
import postomat.routing.regions
import postomat.routing.score

fun Application.configureRouting() {
    install(CORS) {
        allowHost("*")
        allowHeaders { true }
    }

    apiRouting {
        regions()
        postomat()
        score()
    }
}

