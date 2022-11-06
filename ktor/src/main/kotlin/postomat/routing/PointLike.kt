package postomat.routing

import io.github.reactivecircus.cache4k.Cache
import me.plony.empty.id
import me.plony.postomat.PostomatType
import me.plony.regions.Contains
import me.plony.regions.Region
import me.plony.regions.regionOrNull
import stubs.Stubs
import kotlin.time.Duration.Companion.hours

interface PointLike {
    val point: Point
    val regionId: Long?
    val type: PostomatType
    val score: Double
}

fun  <T: PointLike> List<T>.applyFilter(
    filter: Filter
) = filter {
    !(filter.mo?.isNotEmpty() == true && it.regionId !in filter.mo) &&
            !(filter.type != null && it.type != filter.type) &&
            !(filter.scoreRange?.isNotEmpty() == true && it.score !in filter.scoreRange.let { it[0]..it[1] })
}

