package xyz.zelda.company.dto

class JobDto {
    @NotBlank
    private val id: String? = null

    @NotBlank
    private val companyId: String? = null

    @NotBlank
    private val teamId: String? = null

    @NotBlank
    private val name: String? = null
    private val archived = false

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @NotBlank
    private val color: String? = null
}