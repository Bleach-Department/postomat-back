import kotlinx.coroutines.flow.*
import me.plony.empty.Empty
import me.plony.empty.Id
import me.plony.geo.Point
import me.plony.geo.point
import me.plony.postomat.AddRequest
import me.plony.postomat.Postomat
import me.plony.postomat.PostomatServiceGrpcKt
import me.plony.postomat.postomat
import me.plony.regions.regionOrNull
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import stubs.Stubs

class PostomatService : PostomatServiceGrpcKt.PostomatServiceCoroutineImplBase() {
    override suspend fun add(request: AddRequest): Postomat {
        val region = Stubs.region.getRegionContaining(request.point)
            .regionOrNull

        return transaction {
            database.Postomat.new {
                lat = request.point.lat
                long = request.point.long
                this.region = region?.id
                this.type = request.type
            }
        }.toProto()
    }

    override fun getAll(request: Empty): Flow<Postomat> {
        return transaction {
            database.Postomat.all().map { it.toProto()
            }.asFlow()}
    }

    override suspend fun remove(request: Id): Empty {
        transaction {
            database.Postomat.findById(request.id)
                ?.delete()
        }
        return Empty.getDefaultInstance()
    }

    override suspend fun removeAll(request: Empty): Empty {
        transaction {
            database.Postomats.deleteAll()
        }
        return Empty.getDefaultInstance()
    }
}

private fun database.Postomat.toProto(): Postomat = postomat {
    id = this@toProto.id.value
    point = point {
        lat = this@toProto.lat
        long = this@toProto.long
    }
    regionId = this@toProto.region!!
}
