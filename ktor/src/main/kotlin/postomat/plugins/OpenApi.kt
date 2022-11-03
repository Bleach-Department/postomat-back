package postomat.plugins

import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.json.schema.KotlinXSchemaConfigurator
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.server.Server
import io.ktor.server.application.*
import java.net.URI

fun Application.configureOpenApi() {
    install(NotarizedApplication()) {
        spec = OpenApiSpec(
            info = Info(
                "Postomat",
                "0.0.1",

            ),
            servers = mutableListOf(
                Server(
                    url = URI("https://plony"),
                    description = "API server"
                ),
            )
        )
        // Adds support for @Transient and @SerialName
        // If you are not using them this is not required.
        schemaConfigurator = KotlinXSchemaConfigurator()
    }
}