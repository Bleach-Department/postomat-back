package stubs

import io.grpc.ManagedChannelBuilder

object Channels {
    val regionChannel = ManagedChannelBuilder
        .forAddress("regions", Ports.region)
        .usePlaintext()
        .build()
}