package stubs

object Ports {
    val model = System.getenv("MODEL_PORT").toInt()
    val location = System.getenv("LOCATION_PORT").toInt()
    val region = System.getenv("REGION_PORT").toInt()
    val postomat = System.getenv("POSTOMAT_PORT").toInt()
}