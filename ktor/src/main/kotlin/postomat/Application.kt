package postomat

import io.ktor.server.application.*
import postomat.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
//    configureSecurity()
    configureOpenApi()
    configureSerialization()
    configureSockets()
    configureRouting()
}
