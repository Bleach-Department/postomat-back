package postomat.plugins

import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.openAPIGen
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureOpenApi() {
    install(OpenAPIGen) {
        serveOpenApiJson = true
        serveSwaggerUi = true
        server("https://plony.ru")
        info {
            title = "Postomat"
        }
    }
    routing {
        get("/openapi.json") {
            call.respond(application.openAPIGen.api.serialize())
        }
        get("/docs") {
            call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
        }
    }
}