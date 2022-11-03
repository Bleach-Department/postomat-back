import database.Postomats
import io.grpc.netty.NettyServerBuilder
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import stubs.Ports
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.concurrent.thread

fun main() {
    configureDatabase()
    val server = NettyServerBuilder
        .forAddress(InetSocketAddress(InetAddress.getByName("0.0.0.0"), Ports.postomat))
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
    Database.connect(System.getenv("DATABASE_CONNECTOR"),
        driver = "org.postgresql.Driver",
        user = System.getenv("DATABASE_USER"),
        password = System.getenv("DATABASE_PASSWORD"))

    transaction {
        SchemaUtils.create(
            Postomats
        )
    }
}