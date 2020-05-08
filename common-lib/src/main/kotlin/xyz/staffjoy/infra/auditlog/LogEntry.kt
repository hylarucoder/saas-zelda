package xyz.staffjoy.common.auditlog

import com.github.structlog4j.IToLog
import lombok.Builder
import lombok.Data

@Data
@Builder
class LogEntry : IToLog {
    private val currentUserId: String? = null
    private val companyId: String? = null
    private val teamId: String? = null
    private val authorization: String? = null
    private val targetType: String? = null
    private val targetId: String? = null
    private val originalContents: String? = null
    private val updatedContents: String? = null
    override fun toLog(): Array<String?> {
        return arrayOf(
                "auditlog", "true",
                "currentUserId", currentUserId,
                "companyId", companyId,
                "teamId", teamId,
                "authorization", authorization,
                "targetType", targetType,
                "targetId", targetId,
                "originalContents", originalContents,
                "updatedContents", updatedContents
        )
    }
}