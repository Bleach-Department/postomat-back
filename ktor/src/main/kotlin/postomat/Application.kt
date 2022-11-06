package postomat

import io.ktor.server.application.*
import postomat.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
//    configureSecurity()
    configureOpenApi()
    configureSerialization()
    configureSockets()
    configureRouting()
}
