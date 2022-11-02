package stubs

object Ports {
    val region = System.getenv("REGION_PORT").toInt()
    val postomat = System.getenv("POSTOMAT_PORT").toInt()
}