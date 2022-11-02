import database.*
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Polygon
import io.github.dellisd.spatialk.geojson.Position
import io.grpc.ServerBuilder
import io.grpc.netty.NettyServerBuilder
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import stubs.Ports
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import kotlin.concurrent.thread

fun main() {
    configureDatabase()
    val server = NettyServerBuilder
        .forAddress(InetSocketAddress(InetAddress.getByName("0.0.0.0"), Ports.region))
        .addService(RegionService())
        .build()

    server.start()
    println("Region server started at port=${Ports.region}")
    Runtime.getRuntime().addShutdownHook(thread(false) {
        println("Shutting down server due to JVM closing")
        server.shutdown()
    })
    server.awaitTermination()
}


fun configureDatabase() {
    Database.connect(System.getenv("DATABASE_CONNECTOR").also { println(it) }, driver = "org.postgresql.Driver",
        user = System.getenv("DATABASE_USER").also { println(it) }, password = System.getenv("DATABASE_PASSWORD").also { println(it) })

    transaction {
        SchemaUtils.create(
            Regions,
            Polygons,
            Rings,
            Points,
        )
    }
    transaction {
        if (Region.count() == 0L) {
            val featureCollection = FeatureCollection.fromJson(
                ClassLoader.getSystemResource("ao.geojson")
                    .readText()
            )

            featureCollection.features
                .forEach {
                    val region = Region.new {
                        name = it.properties["NAME"]!!.jsonPrimitive.content
                        abbr = it.properties["ABBR"]!!.jsonPrimitive.content
                        type = RegionType.District
                    }
                    when (val geometry = it.geometry) {
                        is MultiPolygon -> {
                            geometry.coordinates.forEach {
                                createPolygon(region, it)
                            }
                        }
                        is Polygon -> {
                            createPolygon(region, geometry.coordinates)
                        }
                        else -> error("${it.geometry} not supported")
                    }

                }
        }
    }
}

private fun createPolygon(
    region: Region,
    it: List<List<Position>>
) {
    val polygon = database.Polygon.new {
        this.region = region
    }
    it.forEach {
        val ring = Ring.new {
            this.polygon = polygon
        }
        it.forEach {
            Point.new {
                this.ring = ring
                lat = it.latitude
                long = it.longitude
            }
        }
    }
}
