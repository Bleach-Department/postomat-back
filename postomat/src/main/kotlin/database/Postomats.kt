package database

import me.plony.postomat.PostomatType
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Postomats : LongIdTable() {
    val lat = double("lat")
    val long = double("long")
    val region = long("region").nullable()
    val type = enumeration<PostomatType>("type")
}

class Postomat(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Postomat>(Postomats)

    var lat by Postomats.lat
    var long by Postomats.long
    var region by Postomats.region
    var type by Postomats.type
}