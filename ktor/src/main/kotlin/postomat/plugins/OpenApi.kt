package postomat.plugins

//import io.bkbn.kompendium.core.plugin.NotarizedApplication
//import io.bkbn.kompendium.json.schema.KotlinXSchemaConfigurator
//import io.bkbn.kompendium.oas.OpenApiSpec
//import io.bkbn.kompendium.oas.info.Info
//import io.bkbn.kompendium.oas.server.Server
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.openAPIGen
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.URI

fun Application.configureOpenApi() {
//    install(NotarizedApplication()) {
//        spec = OpenApiSpec(
//            info = Info(
//                "Postomat",
//                "0.0.1",
//
//            ),
//            servers = mutableListOf(
//                Server(
//                    url = URI("https://plony"),
//                    description = "API server"
//                ),
//            )
//        )
//        // Adds support for @Transient and @SerialName
//        // If you are not using them this is not required.
//        schemaConfigurator = KotlinXSchemaConfigurator()
//    }

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