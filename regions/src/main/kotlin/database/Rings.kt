package database

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Rings : LongIdTable() {
    val polygon = reference("polygon", Polygons)
}

class Ring(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Ring>(Rings)

    var polygon by Polygon referencedOn Rings.polygon
    val points by Point referrersOn Points.ring
}