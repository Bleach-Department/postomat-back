package postomat.routing

import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import me.plony.postomat.PostomatType

/**
 * Data type для десериализации параметров сериализации.
 * Принимает в себя:
 * ao - административный округ
 * mo - район
 * scoreRange - список из двух элементов: начало, конец
 * type - тип пунктов
 * distance - TODO: изменяет метрики модели
 */
data class Filter(
    @QueryParam("Administrative Districts")
    val ao: List<Long>? = null,
    @QueryParam("Regular Districts")
    val mo: List<Long>? = null,
    @QueryParam("Score")
    val scoreRange: List<Double>? = null,
    @QueryParam("Distance as a metric for ML")
    val distance: Int? = null,
    @QueryParam("Postomat Type")
    val type: List<PostomatType>? = null
)