package stubs

import ModelGrpcKt
import me.plony.postomat.PostomatServiceGrpcKt
import me.plony.regions.RegionsGrpcKt
import stubs.Channels.locationChannel
import stubs.Channels.modelChannel
import stubs.Channels.postomatChannel
import stubs.Channels.regionChannel

object Stubs {
    val region = RegionsGrpcKt.RegionsCoroutineStub(regionChannel)
    val postomat = PostomatServiceGrpcKt.PostomatServiceCoroutineStub(postomatChannel)
    val location = TransformLocationGrpcKt.TransformLocationCoroutineStub(locationChannel)
    val model = ModelGrpcKt.ModelCoroutineStub(modelChannel)
}