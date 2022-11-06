package postomat.routing

import me.plony.postomat.PostomatType

/**
 * Интерфейс для применения фильтров. Фильтры должны
 * применяться на любые точки на карте
 */
interface PointLike {
    val point: Point
    val regionId: Long?
    val type: PostomatType
    val score: Double
}

fun  <A, T: PointLike> List<A>.applyFilter(
    filter: Filter,
    map: (A) -> T,
) = filter {
    val point = map(it)
    point.regionId != null &&
            !(filter.mo?.isNotEmpty() == true && point.regionId !in filter.mo) &&
            !(filter.type?.isNotEmpty() != null && point.type !in filter.type) &&
            !(filter.scoreRange?.isNotEmpty() == true && point.score !in filter.scoreRange.let { it[0]..it[1] })
}

