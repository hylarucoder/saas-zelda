package xyz.zelda.web.config

import org.apache.commons.io.IOUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import java.io.IOException
import java.io.InputStream
import javax.annotation.PostConstruct

@Component
class AssetLoader {
    lateinit var imageBase64: String
        private set

    @PostConstruct
    @Throws(IOException::class)
    fun init() {

        // load image
        val imageFileInputStream = imageFile
        val encodedImage: ByteArray = IOUtils.toByteArray(imageFileInputStream)
        val base64EncodedImage: ByteArray = Base64Utils.encode(encodedImage)
        imageBase64 = String(base64EncodedImage)
    }

    @get:Throws(IOException::class)
    private val imageFile: InputStream
        private get() = ClassPathResource(IMAGE_FILE_PATH).getInputStream()

    companion object {
        const val IMAGE_FILE_PATH = "static/assets/images/staffjoy_coffee.png"
    }
}