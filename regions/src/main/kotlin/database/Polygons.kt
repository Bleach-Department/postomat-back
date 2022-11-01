package database

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Polygons : LongIdTable() {
    val region = reference("region", Regions)
}

class Polygon(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Polygon>(Polygons)
    var region by Region referencedOn Polygons.region
    val rings by Ring referrersOn Rings.polygon
}