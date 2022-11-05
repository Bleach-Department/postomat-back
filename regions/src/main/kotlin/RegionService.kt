import com.google.protobuf.StringValue
import com.google.protobuf.any
import database.Ring
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import me.plony.empty.Empty
import me.plony.empty.Id
import me.plony.geo.*
import me.plony.regions.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.Polygon
import java.awt.geom.Point2D
import kotlin.coroutines.CoroutineContext

class RegionService : RegionsGrpcKt.RegionsCoroutineImplBase() {
    override val context: CoroutineContext = Dispatchers.IO

    override fun geometryOfRegion(requests: Flow<Id>): Flow<MultiPolygon> = flow{
        requests.mapNotNull {
            transaction {
                database.Region.findById(it.id)
                    ?.asMultiPolygon()
            }
        }.let { emitAll(it) }
    }

    override fun getRegions(request: Empty): Flow<Region> = flow {
        val regions = transaction {
            database.Region.all()
                .map {
                    it.toLittleProto()
                }
        }
        emitAll(regions.asFlow())
    }

    override suspend fun getRegionsGeoJson(request: Empty): FeatureCollection {
        val features = transaction {
            database.Region.all()
                .map { it.toProto() }
        }
        return featureCollection {
            this.features.addAll(features)
        }
    }

    override suspend fun getRegionContaining(request: Point): Contains {
        val point = request.awtPoint()
        val feature = transaction {
            database.Region.all()
                .map { it to it.toProto() }
        }.firstOrNull { (_, feature) ->
            feature.containsPoint(point)
        }
        return contains {
            if (feature != null) {
                region = transaction {
                    feature.first.toLittleProto()
                }
            }
        }
    }

    private fun Feature.containsPoint(point: Point2D.Double): Boolean {
        val polygons = geometry.polygonsList
        for (polygon in polygons) {
            val big = polygon.ringsList[0]
            if (big.awtPolygon().contains(point) &&
                polygon.ringsList.drop(1).all { point !in it.awtPolygon() })
                return true
        }
        return false
    }


}

private fun Point.awtPoint() = Point2D.Double(long * 1000, lat * 1000)

private fun PointList.awtPolygon() =
    Polygon(
        pointsList.map { (it.long * 1000).toInt() }.toIntArray(),
        pointsList.map { (it.lat * 1000).toInt() }.toIntArray(),
        pointsCount
    )

private fun database.Region.toLittleProto() = region {
    id = this@toLittleProto.id.value
    name = this@toLittleProto.name
    parent?.id?.value?.let {
        parentId = it
    }
}

private fun database.Point.toProto() = point {
    lat = this@toProto.lat
    long = this@toProto.long
}
private fun Ring.toProto() = pointList {
    points.addAll(this@toProto.points.map { it.toProto() })
}
private fun database.Polygon.toProto() = polygon {
    rings.addAll(this@toProto.rings.map {
        it.toProto()
    })
}
private fun database.Region.toProto() = feature {
    properties.add(property {
        name = "id"
        value = this@toProto.id.toString()
    })
    properties.add(property {
        name = "name"
        value = this@toProto.name
    })

    properties.add(property {
        name = "abbr"
        value = this@toProto.abbr
    })
    if (parent != null) {
        properties.add(property {
            name = "parent_id"
            value = this@toProto.parent!!.id.toString()
        })
        properties.add(property {
            name = "parent_name"
            value = this@toProto.parent!!.name
        })
        properties.add(property {
            name = "parent_abbr"
            value = this@toProto.parent!!.abbr
        })
    }

    geometry = asMultiPolygon()
}

private fun database.Region.asMultiPolygon() = multiPolygon {
    polygons.addAll(this@asMultiPolygon.polygons.map {
        it.toProto()
    })
}