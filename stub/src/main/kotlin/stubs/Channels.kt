package stubs

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

object Channels {
    val regionChannel: ManagedChannel = ManagedChannelBuilder
        .forAddress("regions", Ports.region)
        .usePlaintext()
        .build()
    val postomatChannel: ManagedChannel = ManagedChannelBuilder
        .forAddress("postomat", Ports.postomat)
        .usePlaintext()
        .build()
    val locationChannel: ManagedChannel = ManagedChannelBuilder
        .forAddress("178.170.197.129", Ports.location)
        .usePlaintext()
        .build()
    val modelChannel: ManagedChannel = ManagedChannelBuilder
        .forAddress("178.170.197.129", Ports.model)
        .usePlaintext()
        .build()
}