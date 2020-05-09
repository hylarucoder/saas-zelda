package xyz.zelda.web.view

import lombok.Builder
import lombok.Getter
import lombok.Setter

@Getter
@Setter
class LoginPage // lombok inheritance workaround, details here: https://www.baeldung.com/lombok-builder-inheritance
@Builder(builderMethodName = "childBuilder") constructor(title: String?, description: String?, templateName: String?, cssId: String?, version: String?, private val denied: Boolean, private val previousEmail: String, private val returnTo: String) : Page(title, description, templateName, cssId, version) 