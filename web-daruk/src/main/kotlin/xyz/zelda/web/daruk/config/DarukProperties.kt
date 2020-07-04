package xyz.zelda.web.daruk.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("daruk")
data class DarukProperties(var title: String, val banner: Banner) {
    data class Banner(val title: String? = null, val content: String)
}