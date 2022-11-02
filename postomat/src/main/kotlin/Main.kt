import io.grpc.netty.NettyServerBuilder
import org.jetbrains.exposed.sql.Database
import stubs.Ports
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.concurrent.thread

fun main() {
    configureDatabase()
    val server = NettyServerBuilder
        .forAddress(InetSocketAddress(InetAddress.getByName("0.0.0.0"), Ports.region))
        .addService(PostomatService())
        .build()

    server.start()
    println("Postomat server started at port=${Ports.postomat}")
    Runtime.getRuntime().addShutdownHook(thread(false) {
        println("Shutting down server due to JVM closing")
        server.shutdown()
    })
    server.awaitTermination()
}

fun configureDatabase() {
    Database.connect(System.getenv("DATABASE_CONNECTOR").also { println(it) },
        driver = "org.postgresql.Driver",
        user = System.getenv("DATABASE_USER").also { println(it) },
        password = System.getenv("DATABASE_PASSWORD").also { println(it) })
}