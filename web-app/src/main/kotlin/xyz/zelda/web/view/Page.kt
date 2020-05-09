package xyz.zelda.web.view

import lombok.*

@Getter
@Setter
@Builder
open class Page {
    private val title: String? = null
    private val description: String? = null
    private val templateName: String? = null
    private val cssId: String? = null

    @Builder.Default
    private val version = "3.0"
}