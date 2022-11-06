package postomat.routing

import io.github.reactivecircus.cache4k.Cache
import me.plony.empty.id
import me.plony.postomat.PostomatType
import me.plony.regions.Region
import stubs.Stubs
import kotlin.time.Duration.Companion.hours

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

val pointLikeCache = Cache.Builder()
    .expireAfterWrite(1.hours)
    .build<PointLike, Region>()

suspend fun checkIfRegionNotInAO(
    filter: Filter,
    it: PointLike,
) = filter.ao != null &&
       pointLikeCache.get(it) {
           Stubs.region.getRegion(id { id = it.regionId!! })
       }.let { !(it.hasParentId() && it.parentId in filter.ao) }

