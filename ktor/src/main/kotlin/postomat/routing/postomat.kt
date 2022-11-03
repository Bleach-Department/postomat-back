package postomat.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable
import me.plony.empty.Empty
import me.plony.empty.id
import me.plony.geo.point
import me.plony.postomat.Postomat
import stubs.Stubs

fun Routing.postomat() {
    route("/postomats") {
        get {
            val postomats = Stubs.postomat.getAll(Empty.getDefaultInstance())
                .toList()
                .map { it.toDTO() }

            call.respond(postomats)
        }

        post {
            val point = call.receive<Point>()
            val postomat = Stubs.postomat.add(point {
                lat = point.lat
                long = point.long
            }).toDTO()
            call.respond(postomat)
        }
        delete {
            val id = call.receive<Id>()
            Stubs.postomat.remove(id { this.id = id.id })
            call.respond(HttpStatusCode.OK)
        }
    }
}

@Serializable
data class Id(
    val id: Long
)

@Serializable
data class Point(
    val lat: Double,
    val long: Double
)

@Serializable
data class PostomatDTO(
    val id: Long,
    val point: Point,
    val regionId: Long?
)

private fun Postomat.toDTO() = PostomatDTO(id, Point(point.lat, point.long), if (hasRegionId()) regionId else null)
