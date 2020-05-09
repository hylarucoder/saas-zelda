package xyz.zelda.company.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.zelda.company.dto.AdminEntries
import xyz.zelda.company.dto.AdminOfList
import xyz.zelda.company.dto.CompanyDto
import xyz.zelda.company.dto.DirectoryEntryDto
import xyz.zelda.company.model.Admin
import xyz.zelda.company.repo.AdminRepo
import xyz.zelda.company.service.helper.ServiceHelper
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auditlog.LogEntry
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.exception.ServiceException

@Service
class AdminService {
    @Autowired
    var adminRepo: AdminRepo? = null

    @Autowired
    var companyService: CompanyService? = null

    @Autowired
    var directoryService: DirectoryService? = null

    @Autowired
    var serviceHelper: ServiceHelper? = null
    fun listAdmins(companyId: String?): AdminEntries? {
        // validate and will throw exception if not found
        companyService!!.getCompany(companyId)
        val adminEntries: AdminEntries = AdminEntries.builder()
                .companyId(companyId)
                .build()
        val admins = adminRepo!!.findByCompanyId(companyId)
        for (admin in admins!!) {
            val directoryEntryDto: DirectoryEntryDto? = directoryService!!.getDirectoryEntry(companyId, admin.getUserId())
            adminEntries.getAdmins().add(directoryEntryDto)
        }
        return adminEntries
    }

    fun getAdmin(companyId: String?, userId: String?): DirectoryEntryDto? {
        // validate and will throw exceptions if not found
        companyService!!.getCompany(companyId)
        val admin = adminRepo!!.findByCompanyIdAndUserId(companyId, userId) ?: return null
        return directoryService!!.getDirectoryEntry(companyId, userId)
    }

    fun deleteAdmin(companyId: String?, userId: String?) {
        // validate and will throw exception if not found
        getAdmin(companyId, userId)
        try {
            adminRepo!!.deleteAdmin(companyId, userId)
        } catch (ex: Exception) {
            val errMsg = "could not delete the admin"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("admin")
                .targetId(userId)
                .companyId(companyId)
                .teamId("")
                .build()
        logger.info("removed admin", auditLog)
        serviceHelper!!.trackEventAsync("admin_deleted")
    }

    fun createAdmin(companyId: String?, userId: String?): DirectoryEntryDto? {
        val existing = adminRepo!!.findByCompanyIdAndUserId(companyId, userId)
        if (existing != null) {
            throw ServiceException("user is already an admin")
        }
        val directoryEntryDto: DirectoryEntryDto? = directoryService!!.getDirectoryEntry(companyId, userId)
        try {
            val admin: Admin = builder()
                    .companyId(companyId)
                    .userId(userId)
                    .build()
            adminRepo.save(admin)
        } catch (ex: Exception) {
            val errMsg = "could not create the admin"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("admin")
                .targetId(userId)
                .companyId(companyId)
                .teamId("")
                .build()
        logger.info("added admin", auditLog)
        serviceHelper!!.trackEventAsync("admin_created")
        return directoryEntryDto
    }

    fun getAdminOf(userId: String?): AdminOfList? {
        val adminOfList: AdminOfList = AdminOfList.builder()
                .userId(userId)
                .build()
        val admins = adminRepo!!.findByUserId(userId)
        for (admin in admins!!) {
            val companyDto: CompanyDto? = companyService!!.getCompany(admin.getCompanyId())
            adminOfList.getCompanies().add(companyDto)
        }
        return adminOfList
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(AdminService::class.java)
    }
}