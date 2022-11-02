import com.google.protobuf.*
import com.google.protobuf.Any
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.serialization.json.JsonPrimitive

fun me.plony.geo.Feature.toGeo() =
    Feature(
        MultiPolygon(
            geometry.polygonsList.map { it.toGeo() }
        ),
        propertiesList.associate {
            it.name to JsonPrimitive(it.value)
        }
    )

fun me.plony.geo.Polygon.toGeo() = ringsList.map {
    it.pointsList.map {
        it.toGeo()
    }
}

fun me.plony.geo.Point.toGeo() = Position(long, lat)
