package stubs

import me.plony.regions.RegionsGrpcKt
import stubs.Channels.regionChannel

object Stubs {
    val region = RegionsGrpcKt.RegionsCoroutineStub(regionChannel)
}