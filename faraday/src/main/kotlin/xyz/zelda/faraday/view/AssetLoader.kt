package xyz.zelda.faraday.view

import org.apache.commons.io.IOUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import java.io.IOException
import java.io.InputStream
import javax.annotation.PostConstruct

@Component
class AssetLoader {
    final var imageBase64: String? = null
        private set
    final lateinit var faviconFile: ByteArray
        private set

    @PostConstruct
    @Throws(IOException::class)
    fun init() {

        // load image
        val imageFileInputStream = getImageFile(IMAGE_FILE_PATH)
        val encodedImage: ByteArray = IOUtils.toByteArray(imageFileInputStream)
        val base64EncodedImage = Base64Utils.encode(encodedImage)
        imageBase64 = String(base64EncodedImage)

        // load favicon
        val faviconFileInputStream = getImageFile(FAVICON_FILE_PATH)
        faviconFile = IOUtils.toByteArray(faviconFileInputStream)
    }

    @Throws(IOException::class)
    private fun getImageFile(path: String): InputStream {
        return ClassPathResource(path).inputStream
    }

    companion object {
        const val IMAGE_FILE_PATH = "static/assets/images/staffjoy_coffee.png"
        const val FAVICON_FILE_PATH = "static/assets/images/favicon.ico"
    }
}