package database

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Points : LongIdTable() {
    val ring = reference("ring", Rings)
    val long = double("long")
    val lat = double("lat")
}

class Point(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Point>(Points)

    var ring by Ring referencedOn Points.ring
    var long by Points.long
    var lat by Points.lat
}