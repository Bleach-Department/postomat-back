package postomat.routing

import com.papsign.ktor.openapigen.annotations.parameters.QueryParam

data class Filter(
    @QueryParam("Administrative Districts")
    val ao: List<Long>? = null,
    @QueryParam("Regular Districts")
    val mo: List<Long>? = null,
    @QueryParam("Score")
    val scoreRange: List<Double>? = null,
    @QueryParam("Distance as a metric for ML")
    val distance: Int? = null,
)