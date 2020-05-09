package xyz.zelda.company.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import xyz.zelda.company.service.DirectoryService
import xyz.zelda.company.service.PermissionService
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Authorize

@RestController
@RequestMapping("/v1/company/directory")
@Validated
class DirectoryController {
    @Autowired
    var directoryService: DirectoryService? = null

    @Autowired
    var permissionService: PermissionService? = null

    @PostMapping(path = "/create")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE])
    fun createDirectory(@RequestBody @Validated request: NewDirectoryEntry?): GenericDirectoryResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(request.getCompanyId())
        }
        val directoryEntryDto: DirectoryEntryDto? = directoryService!!.createDirectory(request)
        return GenericDirectoryResponse(directoryEntryDto)
    }

    @GetMapping(path = "/list")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun listDirectories(@RequestParam companyId: String?,
                        @RequestParam(defaultValue = "0") offset: Int,
                        @RequestParam(defaultValue = "0") limit: Int): ListDirectoryResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(companyId)
        }
        val directoryList: DirectoryList? = directoryService!!.listDirectory(companyId, offset, limit)
        return ListDirectoryResponse(directoryList)
    }

    @GetMapping(path = "/get")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WHOAMI_SERVICE, AuthConstant.AUTHORIZATION_WWW_SERVICE])
    fun getDirectoryEntry(@RequestParam companyId: String?, @RequestParam userId: String?): GenericDirectoryResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            // user can access their own entry
            if (userId != AuthContext.getUserId()) {
                permissionService!!.checkPermissionCompanyAdmin(companyId)
            }
        }
        val directoryEntryDto: DirectoryEntryDto? = directoryService!!.getDirectoryEntry(companyId, userId)
        return GenericDirectoryResponse(directoryEntryDto)
    }

    @PutMapping(path = "/update")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun updateDirectoryEntry(@RequestBody @Validated request: DirectoryEntryDto?): GenericDirectoryResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(request.getCompanyId())
        }
        val directoryEntryDto: DirectoryEntryDto? = directoryService!!.updateDirectoryEntry(request)
        return GenericDirectoryResponse(directoryEntryDto)
    }

    @GetMapping(path = "/get_associations")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun getAssociations(@RequestParam companyId: String?,
                        @RequestParam(defaultValue = "0") offset: Int,
                        @RequestParam(defaultValue = "0") limit: Int): GetAssociationResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(companyId)
        }
        val associationList: AssociationList? = directoryService!!.getAssociations(companyId, offset, limit)
        return GetAssociationResponse(associationList)
    }
}