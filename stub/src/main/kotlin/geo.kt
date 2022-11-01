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

fun Any.toJsonElement() = when {
    `is`(FloatValue::class.java) -> JsonPrimitive(unpack(FloatValue::class.java).value)
    `is`(Int64Value::class.java) -> JsonPrimitive(unpack(FloatValue::class.java).value)
    `is`(Int32Value::class.java) -> JsonPrimitive(unpack(Int32Value::class.java).value)
    `is`(UInt32Value::class.java) -> JsonPrimitive(unpack(UInt32Value::class.java).value)
    `is`(UInt64Value::class.java) -> JsonPrimitive(unpack(UInt64Value::class.java).value)
    `is`(StringValue::class.java) -> JsonPrimitive(unpack(StringValue::class.java).value)
    `is`(BoolValue::class.java) -> JsonPrimitive(unpack(BoolValue::class.java).value)
    else -> error("Unsupported type of $this")
}


fun me.plony.geo.Polygon.toGeo() = ringsList.map {
    it.pointsList.map {
        it.toGeo()
    }
}

fun me.plony.geo.Point.toGeo() = Position(long, lat)
