package xyz.zelda.gateway.core.balancer

import xyz.zelda.gateway.core.balancer.LoadBalancer
import java.util.concurrent.ThreadLocalRandom

class RandomLoadBalancer : LoadBalancer {
    override fun chooseDestination(destinations: List<String>): String {
        val hostIndex = if (destinations.size == 1) 0 else ThreadLocalRandom.current().nextInt(0, destinations.size)
        return destinations[hostIndex]
    }
}