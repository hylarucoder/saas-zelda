package xyz.zelda.web.view.error

import lombok.Builder
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Builder
class ErrorPage {
    private val title // Used in <title> and <h1>
            : String? = null
    private val explanation // Tell the user what's wrong
            : String? = null
    private val headerCode // http status code
            = 0
    private val linkText // Where do you want user to go?
            : String? = null
    private val linkHref // what's the link?
            : String? = null
    private val sentryErrorId // What do we track the view as on the backend?
            : String? = null
    private val sentryPublicDsn // Config for app
            : String? = null
    private val imageBase64: String? = null
}