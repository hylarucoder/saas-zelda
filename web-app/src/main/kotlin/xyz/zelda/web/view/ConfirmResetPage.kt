package xyz.zelda.web.view

import lombok.Builder
import lombok.Getter
import lombok.Setter

@Getter
@Setter
class ConfirmResetPage // lombok inheritance workaround, details here: https://www.baeldung.com/lombok-builder-inheritance
@Builder(builderMethodName = "childBuilder") constructor(title: String?,
                                                         description: String?,
                                                         templateName: String?,
                                                         cssId: String?,
                                                         version: String?,
                                                         private val errorMessage: String,
                                                         private val token: String) : Page(title, description, templateName, cssId, version) 