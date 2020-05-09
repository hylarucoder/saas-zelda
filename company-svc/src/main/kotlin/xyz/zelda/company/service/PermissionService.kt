package xyz.zelda.company.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import io.sentry.SentryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import xyz.zelda.company.model.Admin
import xyz.zelda.company.model.Directory
import xyz.zelda.company.model.Worker
import xyz.zelda.company.repo.AdminRepo
import xyz.zelda.company.repo.DirectoryRepo
import xyz.zelda.company.repo.WorkerRepo
import xyz.zelda.company.service.helper.ServiceHelper
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.auth.PermissionDeniedException

/**
 * Each permission has a public convenience checker, and a private relationship checker.
 * Recall that support users have a different authorization, and will not use these functions.
 *
 * PermissionCompanyAdmin checks that the current user is an admin of the given company
 */
@Service
class PermissionService {
    @Autowired
    private val sentryClient: SentryClient? = null

    @Autowired
    var adminRepo: AdminRepo? = null

    @Autowired
    var workerRepo: WorkerRepo? = null

    @Autowired
    var directoryRepo: DirectoryRepo? = null

    @Autowired
    var serviceHelper: ServiceHelper? = null
    fun checkPermissionCompanyAdmin(companyId: String?) {
        val currentUserId = checkAndGetCurrentUserId()
        var admin: Admin? = null
        try {
            admin = adminRepo!!.findByCompanyIdAndUserId(companyId, currentUserId)
        } catch (ex: Exception) {
            val errMsg = "failed to check company admin permissions"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (admin == null) {
            throw PermissionDeniedException("you do not have admin access to this service")
        }
    }

    // PermissionTeamWorker checks whether a user is a worker of a given team in a given company, or is an admin of that company
    fun checkPermissionTeamWorker(companyId: String?, teamId: String?) {
        val currentUserId = checkAndGetCurrentUserId()

        // Check if company admin
        try {
            val admin = adminRepo!!.findByCompanyIdAndUserId(companyId, currentUserId)
            if (admin != null) { // Admin - allow access
                return
            }
        } catch (ex: Exception) {
            val errMsg = "failed to check company admin permissions"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        var worker: Worker? = null
        try {
            worker = workerRepo!!.findByTeamIdAndUserId(teamId, currentUserId)
        } catch (ex: Exception) {
            val errMsg = "failed to check teamDto member permissions"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (worker == null) {
            throw PermissionDeniedException("you are not associated with this company")
        }
    }

    // PermissionCompanyDirectory checks whether a user exists in the directory of a company. It is the lowest level of security.
    // The user may no longer be associated with a team (i.e. may be a former employee)
    fun checkPermissionCompanyDirectory(companyId: String?) {
        val currentUserId = checkAndGetCurrentUserId()
        var directory: Directory? = null
        try {
            directory = directoryRepo!!.findByCompanyIdAndUserId(companyId, currentUserId)
        } catch (ex: Exception) {
            val errMsg = "failed to check directory existence"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        if (directory == null) {
            throw PermissionDeniedException("you are not associated with this company")
        }
    }

    private fun checkAndGetCurrentUserId(): String? {
        val currentUserId: String = AuthContext.getUserId()
        if (StringUtils.isEmpty(currentUserId)) {
            val errMsg = "failed to find current user id"
            serviceHelper!!.handleErrorAndThrowException(logger, errMsg)
        }
        return currentUserId
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(PermissionService::class.java)
    }
}