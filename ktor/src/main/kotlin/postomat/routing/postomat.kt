package postomat.routing

import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.content.type.binary.BinaryContentTypeParser.respond
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.delete
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable
import me.plony.empty.Empty
import me.plony.empty.id
import me.plony.geo.point
import me.plony.postomat.Postomat
import stubs.Stubs

fun NormalOpenAPIRoute.postomat() {
    route("/postomats") {
        get<Unit, List<PostomatDTO>> {
            val postomats = Stubs.postomat.getAll(Empty.getDefaultInstance())
                .toList()
                .map { it.toDTO() }

            respond(postomats)
        }

        post<Unit, PostomatDTO, Point> { _, point ->
            val postomat = Stubs.postomat.add(point {
                lat = point.lat
                long = point.long
            }).toDTO()

            respond(postomat)
        }
        delete< Id, Unit> { id ->
            Stubs.postomat.remove(id { this.id = id.id })
            respond(Unit)
        }
    }
}

@Response("id of the element")
data class Id(
    @QueryParam("id") val id: Long
)

@Response("Point on the map")
data class Point(
    @QueryParam("latitude") val lat: Double,
    @QueryParam("longitude") val long: Double
)

@Response("Postomat")
data class PostomatDTO(
    val id: Long,
    val point: Point,
    val regionId: Long?
)

private fun Postomat.toDTO() = PostomatDTO(id, Point(point.lat, point.long), if (hasRegionId()) regionId else null)
