package xyz.zelda.company.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import xyz.zelda.company.dto.CompanyDto
import xyz.zelda.company.dto.CompanyList
import xyz.zelda.company.dto.GenericCompanyResponse
import xyz.zelda.company.dto.ListCompanyResponse
import xyz.zelda.company.service.CompanyService
import xyz.zelda.company.service.PermissionService
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.Authorize
import xyz.zelda.infra.validation.Group1
import xyz.zelda.infra.validation.Group2

@RestController
@RequestMapping("/v1/company")
@Validated
class CompanyController {
    @Autowired
    var companyService: CompanyService? = null

    @Autowired
    var permissionService: PermissionService? = null

    @PostMapping(path = "/create")
    @Authorize(value = [AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE])
    fun createCompany(@RequestBody @Validated([Group2::class]) companyDto: CompanyDto?): GenericCompanyResponse? {
        val newCompanyDto: CompanyDto? = companyService!!.createCompany(companyDto)
        return GenericCompanyResponse(newCompanyDto)
    }

    @GetMapping(path = "/list")
    @Authorize(value = [AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun listCompanies(@RequestParam offset: Int, @RequestParam limit: Int): ListCompanyResponse? {
        val companyList: CompanyList? = companyService!!.listCompanies(offset, limit)
        return ListCompanyResponse(companyList)
    }

    @GetMapping(path = "/get")
    @Authorize(value = [AuthConstant.AUTHORIZATION_ACCOUNT_SERVICE, AuthConstant.AUTHORIZATION_BOT_SERVICE, AuthConstant.AUTHORIZATION_WHOAMI_SERVICE, AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER, AuthConstant.AUTHORIZATION_WWW_SERVICE, AuthConstant.AUTHORIZATION_ICAL_SERVICE])
    fun getCompany(@RequestParam("company_id") companyId: String?): GenericCompanyResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyDirectory(companyId)
        }
        val companyDto: CompanyDto? = companyService!!.getCompany(companyId)
        return GenericCompanyResponse(companyDto)
    }

    @PutMapping(path = "/update")
    @Authorize(value = [AuthConstant.AUTHORIZATION_AUTHENTICATED_USER, AuthConstant.AUTHORIZATION_SUPPORT_USER])
    fun updateCompany(@RequestBody @Validated([Group1::class]) companyDto: CompanyDto?): GenericCompanyResponse? {
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            permissionService!!.checkPermissionCompanyAdmin(companyDto.getId())
        }
        val updatedCompanyDto: CompanyDto? = companyService!!.updateCompany(companyDto)
        return GenericCompanyResponse(updatedCompanyDto)
    }
}