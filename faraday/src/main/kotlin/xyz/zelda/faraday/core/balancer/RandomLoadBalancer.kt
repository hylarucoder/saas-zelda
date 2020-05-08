package xyz.zelda.faraday.core.balancer

import java.util.concurrent.ThreadLocalRandom

class RandomLoadBalancer : LoadBalancer {
    override fun chooseDestination(destnations: List<String?>?): String? {
        val hostIndex = if (destnations!!.size == 1) 0 else ThreadLocalRandom.current().nextInt(0, destnations.size)
        return destnations[hostIndex]
    }
}