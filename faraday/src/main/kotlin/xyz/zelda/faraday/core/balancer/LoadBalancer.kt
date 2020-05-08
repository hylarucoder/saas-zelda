package xyz.zelda.faraday.core.balancer

interface LoadBalancer {
    fun chooseDestination(destinations: List<String>): String
}