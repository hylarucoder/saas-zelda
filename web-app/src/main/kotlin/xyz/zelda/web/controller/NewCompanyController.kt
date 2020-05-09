package xyz.zelda.web.controller

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.AccountDto
import xyz.zelda.account.dto.GenericAccountResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.env.EnvConfig
import xyz.zelda.infra.env.EnvConstant
import xyz.zelda.infra.exception.ServiceException
import xyz.zelda.company.client.CompanyClient
import xyz.zelda.company.dto.*
import xyz.zelda.web.service.HelperService
import xyz.zelda.web.view.Constant
import xyz.zelda.web.view.PageFactory
import xyz.zelda.web.controller.LoginController

@Controller
class NewCompanyController {
    @Autowired
    private val pageFactory: PageFactory? = null

    @Autowired
    private val envConfig: EnvConfig? = null

    @Autowired
    private val helperService: HelperService? = null

    @Autowired
    private val accountClient: AccountClient? = null

    @Autowired
    private val companyClient: CompanyClient? = null

    @RequestMapping(value = "/new_company")
    fun newCompany(@RequestParam(value = "name", required = false) name: String?,
                   @RequestParam(value = "timezone", required = false) timezone: String?,
                   @RequestParam(value = "team", required = false) teamName: String?,
                   model: Model): String {
        var timezone = timezone
        var teamName = teamName
        if (StringUtils.isEmpty(AuthContext.getAuthz()) || AuthConstant.AUTHORIZATION_ANONYMOUS_WEB.equals(AuthContext.getAuthz())) {
            return "redirect:/login"
        }
        if (StringUtils.hasText(name)) {
            if (!StringUtils.hasText(timezone)) {
                timezone = DEFAULT_TIMEZONE
            }
            if (!StringUtils.hasText(teamName)) {
                teamName = DEFAULT_TEAM_NAME
            }

            // fetch current userId
            val currentUserId: String = AuthContext.getUserId()
                    ?: throw ServiceException("current userId not found in auth context")
            var currentUser: AccountDto? = null
            var genericAccountResponse: GenericAccountResponse? = null
            genericAccountResponse = try {
                accountClient.getAccount(AuthConstant.AUTHORIZATION_WWW_SERVICE, currentUserId)
            } catch (ex: Exception) {
                val errMsg = "fail to get user account"
                helperService.logException(logger, ex, errMsg)
                throw ServiceException(errMsg, ex)
            }
            currentUser = if (!genericAccountResponse.isSuccess()) {
                helperService.logError(logger, genericAccountResponse.getMessage())
                throw ServiceException(genericAccountResponse.getMessage())
            } else {
                genericAccountResponse.getAccount()
            }

            // Make the company
            var genericCompanyResponse: GenericCompanyResponse? = null
            genericCompanyResponse = try {
                val companyDtoToCreate: CompanyDto = CompanyDto.builder()
                        .name(name)
                        .defaultTimezone(timezone)
                        .defaultDayWeekStarts(DEFAULT_DAYWEEK_STARTS)
                        .build()
                companyClient.createCompany(AuthConstant.AUTHORIZATION_WWW_SERVICE, companyDtoToCreate)
            } catch (ex: Exception) {
                val errMsg = "fail to create company"
                helperService.logException(logger, ex, errMsg)
                throw ServiceException(errMsg, ex)
            }
            if (!genericCompanyResponse.isSuccess()) {
                helperService.logError(logger, genericCompanyResponse.getMessage())
                throw ServiceException(genericCompanyResponse.getMessage())
            }
            val companyDto: CompanyDto = genericCompanyResponse.getCompany()

            // register current user in directory
            var genericDirectoryResponse1: GenericDirectoryResponse? = null
            genericDirectoryResponse1 = try {
                val newDirectoryEntry: NewDirectoryEntry = NewDirectoryEntry.builder()
                        .companyId(companyDto.getId())
                        .email(currentUser.getEmail())
                        .build()
                companyClient.createDirectory(AuthConstant.AUTHORIZATION_WWW_SERVICE, newDirectoryEntry)
            } catch (ex: Exception) {
                val errMsg = "fail to create directory"
                helperService.logException(logger, ex, errMsg)
                throw ServiceException(errMsg, ex)
            }
            if (!genericDirectoryResponse1.isSuccess()) {
                helperService.logError(logger, genericDirectoryResponse1.getMessage())
                throw ServiceException(genericDirectoryResponse1.getMessage())
            }

            // create admin
            var genericDirectoryResponse2: GenericDirectoryResponse? = null
            genericDirectoryResponse2 = try {
                val directoryEntryRequest: DirectoryEntryRequest = DirectoryEntryRequest.builder()
                        .companyId(companyDto.getId())
                        .userId(currentUserId)
                        .build()
                companyClient.createAdmin(AuthConstant.AUTHORIZATION_WWW_SERVICE, directoryEntryRequest)
            } catch (ex: Exception) {
                val errMsg = "fail to create admin"
                helperService.logException(logger, ex, errMsg)
                throw ServiceException(errMsg, ex)
            }
            if (!genericDirectoryResponse2.isSuccess()) {
                helperService.logError(logger, genericDirectoryResponse2.getMessage())
                throw ServiceException(genericDirectoryResponse2.getMessage())
            }

            // create team
            var teamResponse: GenericTeamResponse? = null
            teamResponse = try {
                val createTeamRequest: CreateTeamRequest = CreateTeamRequest.builder()
                        .companyId(companyDto.getId())
                        .name(teamName)
                        .color(DEFAULT_TEAM_COLOR)
                        .build()
                companyClient.createTeam(AuthConstant.AUTHORIZATION_WWW_SERVICE, createTeamRequest)
            } catch (ex: Exception) {
                val errMsg = "fail to create team"
                helperService.logException(logger, ex, errMsg)
                throw ServiceException(errMsg, ex)
            }
            if (!teamResponse.isSuccess()) {
                helperService.logError(logger, teamResponse.getMessage())
                throw ServiceException(teamResponse.getMessage())
            }
            val teamDto: TeamDto = teamResponse.getTeam()

            // register as worker
            var directoryResponse: GenericDirectoryResponse? = null
            directoryResponse = try {
                val workerDto: WorkerDto = WorkerDto.builder()
                        .companyId(companyDto.getId())
                        .teamId(teamDto.getId())
                        .userId(currentUserId)
                        .build()
                companyClient.createWorker(AuthConstant.AUTHORIZATION_WWW_SERVICE, workerDto)
            } catch (ex: Exception) {
                val errMsg = "fail to create worker"
                helperService.logException(logger, ex, errMsg)
                throw ServiceException(errMsg, ex)
            }
            if (!directoryResponse.isSuccess()) {
                helperService.logError(logger, directoryResponse.getMessage())
                throw ServiceException(directoryResponse.getMessage())
            }

            // redirect
            logger.info(java.lang.String.format("new company signup - %s", companyDto))
            val url: String = HelperService.buildUrl("http", "app." + envConfig.getExternalApex())
            helperService.syncUserAsync(currentUserId)
            helperService.trackEventAsync(currentUserId, "freetrial_created")
            if (EnvConstant.ENV_PROD.equals(envConfig.getName()) && !currentUser.isSupport()) {
                // Alert sales of a new account signup
                helperService.sendEmailAsync(currentUser, companyDto)
            }
            return "redirect:$url"
        }
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildNewCompanyPage())
        return Constant.VIEW_NEW_COMPANY
    }

    companion object {
        val logger: ILogger = SLoggerFactory.getLogger(LoginController::class.java)
        const val DEFAULT_TIMEZONE = "UTC"
        const val DEFAULT_DAYWEEK_STARTS = "Monday"
        const val DEFAULT_TEAM_NAME = "Team"
        const val DEFAULT_TEAM_COLOR = "#744fc6"
    }
}