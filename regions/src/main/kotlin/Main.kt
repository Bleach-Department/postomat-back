import database.*
import database.Point
import io.github.dellisd.spatialk.geojson.*
import io.github.dellisd.spatialk.geojson.Polygon
import io.grpc.netty.NettyServerBuilder
import kotlinx.serialization.json.jsonPrimitive
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
    Database.connect(System.getenv("DATABASE_CONNECTOR"), driver = "org.postgresql.Driver",
        user = System.getenv("DATABASE_USER"), password = System.getenv("DATABASE_PASSWORD"))

    transaction {
        SchemaUtils.create(
            Regions,
            Polygons,
            Rings,
            Points,
        )
    }
    loadDataIfNecessary()
}

/**
 * Загружает данные о регионах в базу данных
 */
private fun loadDataIfNecessary() {
    transaction {
        if (Region.count() == 0L) {
            val mo = FeatureCollection.fromJson(
                ClassLoader.getSystemResource("mo.geojson")
                    .readText()
            )
            val ao = FeatureCollection.fromJson(
                ClassLoader.getSystemResource("ao.geojson")
                    .readText()
            )

            ao.features
                .forEach {
                    val region = Region.new {
                        name = it.properties["NAME"]!!.jsonPrimitive.content
                        abbr = it.properties["ABBREV"]!!.jsonPrimitive.content
                        type = RegionType.AdministrativeDistrict
                    }
                    createGeometry(it, region)
                }
            mo.features
                .forEach {
                    val region = Region.new {
                        name = it.properties["NAME"]!!.jsonPrimitive.content
                        abbr = it.properties["ABBREV"]?.jsonPrimitive?.content
                            ?: it.properties["NAME"]!!.jsonPrimitive.content
                        type = RegionType.District
                        parent =
                            Region.find { Regions.name eq it.properties["NAME_AO"]!!.jsonPrimitive.content }.first()
                    }
                    createGeometry(it, region)
                }
        }
    }
}

private fun createGeometry(it: Feature, region: Region) {
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
