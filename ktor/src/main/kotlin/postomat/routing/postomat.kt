package postomat.routing

import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.delete
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.github.reactivecircus.cache4k.Cache
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable
import me.plony.empty.Empty
import me.plony.empty.id
import me.plony.postomat.Postomat
import me.plony.postomat.PostomatType
import stubs.Stubs
import kotlin.time.Duration.Companion.minutes

val postomatCache = Cache.Builder()
    .expireAfterWrite(1.minutes)
    .build<Int, List<PostomatDTO>>()

fun NormalOpenAPIRoute.postomat() {
    route("/postomats") {
        get<Filter, List<PostomatDTO>> { filter ->
            val postomats = postomatCache.get(1) {
                Stubs.postomat.getAll(Empty.getDefaultInstance())
                    .toList()
                    .filter { it.hasRegionId() }
                    .map { it.toDTO() }
            }

            respond(postomats.applyFilter(filter) { it })
        }

        delete<Id, Unit> { id ->
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
@Serializable
data class Point(
    @QueryParam("latitude") val lat: Double,
    @QueryParam("longitude") val long: Double
)

@Response("Postomat")
data class PostomatDTO(
    val id: Long,
    override val point: Point,
    override val regionId: Long?,
    override val type: PostomatType,
    override val score: Double
) : PointLike {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostomatDTO

        if (point != other.point) return false
        if (regionId != other.regionId) return false
        if (type != other.type) return false
        if (score != other.score) return false

        return true
    }

    override fun hashCode(): Int {
        var result = point.hashCode()
        result = 31 * result + (regionId?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + score.hashCode()
        return result
    }
}

private fun Postomat.toDTO() = PostomatDTO(
    id,
    Point(point.lat, point.long),
    if (hasRegionId()) regionId else null,
    type,
    cache.find { it.first.point == Point(point.lat, point.long) }?.first?.score ?: -1.0
)
