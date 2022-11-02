package stubs

import io.grpc.ManagedChannelBuilder

object Channels {
    val regionChannel = ManagedChannelBuilder
        .forAddress("regions", Ports.region)
        .usePlaintext()
        .build()
    val postomatChannel = ManagedChannelBuilder
        .forAddress("postomat", Ports.postomat)
        .usePlaintext()
        .build()
}