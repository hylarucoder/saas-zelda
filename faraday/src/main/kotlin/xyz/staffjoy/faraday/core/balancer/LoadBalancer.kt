package xyz.staffjoy.faraday.core.balancer

interface LoadBalancer {
    fun chooseDestination(destnations: List<String?>?): String?
}