package stubs

import me.plony.postomat.PostomatServiceGrpcKt
import me.plony.regions.RegionsGrpcKt
import stubs.Channels.postomatChannel
import stubs.Channels.regionChannel

object Stubs {
    val region = RegionsGrpcKt.RegionsCoroutineStub(regionChannel)
    val postomat = PostomatServiceGrpcKt.PostomatServiceCoroutineStub(postomatChannel)
}