package xyz.zelda.gateway.core.balancer

interface LoadBalancer {
    fun chooseDestination(destinations: List<String>): String
}