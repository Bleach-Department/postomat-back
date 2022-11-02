package database

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Regions : LongIdTable() {
    val name = text("name")
    val abbr = text("abbr")
    val parent = optReference("parent", Regions).default(null)
    val type = enumeration<RegionType>("type")
}

enum class RegionType {
    District,
    AdministrativeDistrict
}

class Region(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Region>(Regions)

    var name by Regions.name
    var abbr by Regions.abbr
    var parent by Region optionalReferencedOn Regions.parent
    var type by Regions.type

    val polygons by Polygon referrersOn Polygons.region
    val rings get() = polygons.map { it.rings }
}