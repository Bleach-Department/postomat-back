package postomat.routing

import me.plony.empty.id
import me.plony.postomat.PostomatType
import stubs.Stubs

interface PointLike {
    val point: Point
    val regionId: Long?
    val type: PostomatType
    val score: Double
}

suspend fun  <T: PointLike> List<T>.applyFilter(
    filter: Filter
) = filter {
    !(checkIfRegionNotInAO(filter, it)) ||
            !(filter.mo != null && it.regionId !in filter.mo) &&
            !(filter.scoreRange != null && it.score !in filter.scoreRange.let { it[0]..it[1] })
}

suspend fun checkIfRegionNotInAO(
    filter: Filter,
    it: PointLike,
) = filter.ao != null &&
        Stubs.region.getRegion(id { id = it.regionId!! })
            .let { !(it.hasParentId() && it.parentId in filter.ao) || it.id !in filter.ao }
