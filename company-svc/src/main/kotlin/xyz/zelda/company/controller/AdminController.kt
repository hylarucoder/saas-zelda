package xyz.zelda.company.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import xyz.zelda.company.service.AdminService
import xyz.zelda.company.service.PermissionService
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Authorize
import xyz.zelda.infra.auth.PermissionDeniedException
import xyz.zelda.infra.error.ServiceException

@RestController
@RequestMapping("/v1/company/admin")
@Validated
class AdminController {
    @Autowired
    var adminService: AdminService? = null

    @Autowired
    var permissionService: PermissionService? = null

    @GetMapping(path = "/list")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun listAdmins(@RequestParam companyId: String?): ListAdminResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(companyId)
        }
        val adminEntries: AdminEntries? = adminService!!.listAdmins(companyId)
        return ListAdminResponse(adminEntries)
    }

    @GetMapping(path = "/get")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE])
    fun getAdmin(@RequestParam companyId: String?, @RequestParam userId: String?): GenericDirectoryResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(companyId)
        }
        val directoryEntryDto: DirectoryEntryDto = adminService!!.getAdmin(companyId, userId)
                ?: throw ServiceException(ResultCode.NOT_FOUND, "admin relationship not found")
        return GenericDirectoryResponse(directoryEntryDto)
    }

    @PostMapping(path = "/create")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE])
    fun createAdmin(@RequestBody @Validated request: DirectoryEntryRequest?): GenericDirectoryResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(request.getCompanyId())
        }
        val directoryEntryDto: DirectoryEntryDto? = adminService!!.createAdmin(request.getCompanyId(), request.getUserId())
        return GenericDirectoryResponse(directoryEntryDto)
    }

    @DeleteMapping(path = "/delete")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun deleteAdmin(@RequestBody @Validated request: DirectoryEntryRequest?): BaseResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(request.getCompanyId())
        }
        adminService!!.deleteAdmin(request.getCompanyId(), request.getUserId())
        return BaseResponse.builder().build()
    }

    @GetMapping(path = "/admin_of")
    @Authorize(value = [AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE, AuthConstant.AUTHORIZATION_WHOAMI_SERVICE, AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE])
    fun getAdminOf(@RequestParam userId: String?): GetAdminOfResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            if (userId != AuthContext.getUserId()) {
                throw PermissionDeniedException("You do not have access to this service")
            }
        }
        val adminOfList: AdminOfList? = adminService!!.getAdminOf(userId)
        return GetAdminOfResponse(adminOfList)
    }
}