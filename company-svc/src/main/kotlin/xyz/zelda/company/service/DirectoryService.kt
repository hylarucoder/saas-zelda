package xyz.zelda.company.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import xyz.zelda.account.client.AccountClient
import xyz.zelda.account.dto.AccountDto
import xyz.zelda.account.dto.GenericAccountResponse
import xyz.zelda.account.dto.GetOrCreateRequest
import xyz.zelda.bot.dto.OnboardWorkerRequest
import xyz.zelda.company.model.Directory
import xyz.zelda.company.repo.CompanyRepo
import xyz.zelda.company.repo.DirectoryRepo
import xyz.zelda.company.service.helper.ServiceHelper
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auditlog.LogEntry
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.exception.ServiceException

@Service
class DirectoryService {
    @Autowired
    private val companyRepo: CompanyRepo? = null

    @Autowired
    private val directoryRepo: DirectoryRepo? = null

    @Autowired
    private val accountClient: AccountClient? = null

    @Autowired
    private val serviceHelper: ServiceHelper? = null

    @Autowired
    private val workerService: WorkerService? = null

    @Autowired
    private val adminService: AdminService? = null
    fun createDirectory(req: NewDirectoryEntry?): DirectoryEntryDto? {
        val companyExists: Boolean = companyRepo.existsById(req.getCompanyId())
        if (!companyExists) {
            throw ServiceException(ResultCode.NOT_FOUND, "Company with specified id not found")
        }
        val getOrCreateRequest: GetOrCreateRequest = GetOrCreateRequest.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .build()
        var genericAccountResponse: GenericAccountResponse? = null
        try {
            genericAccountResponse = accountClient.getOrCreateAccount(AuthConstant.AUTHORIZATION_COMPANY_SERVICE, getOrCreateRequest)
        } catch (ex: Exception) {
            val errMsg = "could not get or create user"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (!genericAccountResponse.isSuccess()) {
            serviceHelper!!.handleErrorAndThrowException(logger, genericAccountResponse.getMessage())
        }
        val account: AccountDto = genericAccountResponse.getAccount()
        val directoryEntryDto: DirectoryEntryDto = DirectoryEntryDto.builder().internalId(req.getInternalId()).companyId(req.getCompanyId()).build()
        copyAccountToDirectory(account, directoryEntryDto)
        val directoryExists = directoryRepo!!.findByCompanyIdAndUserId(req.getCompanyId(), account.getId()) != null
        if (directoryExists) {
            throw ServiceException("relationship already exists")
        }
        val directory: Directory = builder()
                .companyId(req.getCompanyId())
                .userId(account.getId())
                .internalId(req.getInternalId())
                .build()
        try {
            directoryRepo.save(directory)
        } catch (ex: Exception) {
            val errMsg = "could not create entry"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("directory")
                .targetId(directoryEntryDto.getUserId())
                .companyId(req.getCompanyId())
                .teamId("")
                .updatedContents(directoryEntryDto.toString())
                .build()
        logger.info("updated directory", auditLog)
        val onboardWorkerRequest: OnboardWorkerRequest = OnboardWorkerRequest.builder()
                .companyId(req.getCompanyId())
                .userId(directoryEntryDto.getUserId())
                .build()
        serviceHelper!!.onboardWorkerAsync(onboardWorkerRequest)
        serviceHelper.trackEventAsync("directoryentry_created")
        return directoryEntryDto
    }

    fun listDirectory(companyId: String?, offset: Int, limit: Int): DirectoryList? {
        var limit = limit
        if (limit <= 0) {
            limit = 20
        }
        val directoryList: DirectoryList = DirectoryList.builder().limit(limit).offset(offset).build()
        val pageRequest: PageRequest = PageRequest.of(offset, limit)
        val directoryPage: Page<Directory?>? = directoryRepo!!.findByCompanyId(companyId, pageRequest)
        for (directory in directoryPage.getContent()) {
            val directoryEntryDto: DirectoryEntryDto = DirectoryEntryDto.builder()
                    .companyId(companyId)
                    .internalId(directory.getInternalId())
                    .userId(directory.getUserId())
                    .build()
            var resp: GenericAccountResponse? = null
            try {
                resp = accountClient.getAccount(AuthConstant.AUTHORIZATION_COMPANY_SERVICE, directoryEntryDto.getUserId())
            } catch (ex: Exception) {
                val errMsg = "could not get account"
                serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
            }
            if (!resp.isSuccess()) {
                serviceHelper!!.handleErrorAndThrowException(logger, resp.getMessage())
            }
            val account: AccountDto = resp.getAccount()
            copyAccountToDirectory(account, directoryEntryDto)
            directoryList.getAccounts().add(directoryEntryDto)
        }
        return directoryList
    }

    fun getDirectoryEntry(companyId: String?, userId: String?): DirectoryEntryDto? {
        val directoryEntryDto: DirectoryEntryDto = DirectoryEntryDto.builder().userId(userId).companyId(companyId).build()
        val directory = directoryRepo!!.findByCompanyIdAndUserId(companyId, userId)
                ?: throw ServiceException(ResultCode.NOT_FOUND, "directory entry not found for user in this company")
        directoryEntryDto.setInternalId(directory.getInternalId())
        var resp: GenericAccountResponse? = null
        try {
            resp = accountClient.getAccount(AuthConstant.AUTHORIZATION_COMPANY_SERVICE, userId)
        } catch (ex: Exception) {
            val errMsg = "view fetching account"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (!resp.isSuccess()) {
            serviceHelper!!.handleErrorAndThrowException(logger, resp.getMessage())
        }
        val account: AccountDto = resp.getAccount()
        copyAccountToDirectory(account, directoryEntryDto)
        return directoryEntryDto
    }

    fun updateDirectoryEntry(request: DirectoryEntryDto?): DirectoryEntryDto? {
        val orig: DirectoryEntryDto? = getDirectoryEntry(request.getCompanyId(), request.getUserId())
        var genericAccountResponse1: GenericAccountResponse? = null
        try {
            genericAccountResponse1 = accountClient.getAccount(AuthConstant.AUTHORIZATION_COMPANY_SERVICE, orig.getUserId())
        } catch (ex: Exception) {
            val errMsg = "getting account failed"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (!genericAccountResponse1.isSuccess()) {
            serviceHelper!!.handleErrorAndThrowException(logger, genericAccountResponse1.getMessage())
        }
        val account: AccountDto = genericAccountResponse1.getAccount()
        val accountUpdateRequested = !request.getName().equals(orig.getName()) ||
                !request.getEmail().equals(orig.getEmail()) ||
                !request.getPhoneNumber().equals(orig.getPhoneNumber())
        if (account.isConfirmedAndActive() && accountUpdateRequested) {
            throw ServiceException(ResultCode.PARAM_VALID_ERROR, "this user is active, so they cannot be modified")
        } else if (account.isSupport() && accountUpdateRequested) {
            throw ServiceException(ResultCode.UN_AUTHORIZED, "you cannot change this account")
        }
        if (accountUpdateRequested) {
            account.setName(request.getName())
            account.setPhoneNumber(request.getPhoneNumber())
            account.setEmail(request.getEmail())
            var genericAccountResponse2: GenericAccountResponse? = null
            try {
                genericAccountResponse2 = accountClient.updateAccount(AuthConstant.AUTHORIZATION_COMPANY_SERVICE, account)
            } catch (ex: Exception) {
                val errMsg = "view updating account"
                serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
            }
            if (!genericAccountResponse2.isSuccess()) {
                serviceHelper!!.handleErrorAndThrowException(logger, genericAccountResponse2.getMessage())
            }
            copyAccountToDirectory(account, request)
        }
        try {
            directoryRepo!!.updateInternalIdByCompanyIdAndUserId(request.getInternalId(), request.getCompanyId(), request.getUserId())
        } catch (ex: Exception) {
            val errMsg = "fail to update directory"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("directory")
                .targetId(account.getId())
                .companyId(request.getCompanyId())
                .teamId("")
                .originalContents(orig.toString())
                .updatedContents(request.toString())
                .build()
        logger.info("updated directory entry for account", auditLog)
        if (!request.isConfirmedAndActive() &&
                (!orig.getPhoneNumber().equals(request.getPhoneNumber()) || "" == request.getPhoneNumber() && !orig.getEmail().equals(request.getEmail()))) {
            val onboardWorkerRequest: OnboardWorkerRequest = OnboardWorkerRequest.builder()
                    .companyId(request.getCompanyId())
                    .userId(request.getUserId())
                    .build()
            serviceHelper!!.onboardWorkerAsync(onboardWorkerRequest)
        }
        serviceHelper!!.trackEventAsync("directoryentry_updated")
        return request
    }

    fun getAssociations(companyId: String?, offset: Int, limit: Int): AssociationList? {
        // this handles permissions
        val directoryList: DirectoryList? = listDirectory(companyId, offset, limit)
        val associationList: AssociationList = AssociationList.builder().offset(offset).limit(limit).build()
        for (directoryEntryDto in directoryList.getAccounts()) {
            val association: Association = Association.builder().account(directoryEntryDto).build()
            val workerOfList: WorkerOfList? = workerService!!.getWorkerOf(directoryEntryDto.getUserId())
            for (teamDto in workerOfList.getTeams()) {
                if (teamDto.getCompanyId().equals(companyId)) {
                    association.getTeams().add(teamDto)
                }
                val admin: DirectoryEntryDto? = adminService!!.getAdmin(companyId, directoryEntryDto.getUserId())
                if (admin != null) {
                    association.setAdmin(true)
                } else {
                    association.setAdmin(false)
                }
            }
            associationList.getAccounts().add(association)
        }
        return associationList
    }

    private fun copyAccountToDirectory(a: AccountDto?, d: DirectoryEntryDto?) {
        d.setUserId(a.getId())
        d.setName(a.getName())
        d.setConfirmedAndActive(a.isConfirmedAndActive())
        d.setPhoneNumber(a.getPhoneNumber())
        d.setPhotoUrl(a.getPhotoUrl())
        d.setEmail(a.getEmail())
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(DirectoryService::class.java)
    }
}