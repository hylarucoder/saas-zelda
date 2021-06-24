package xyz.zelda.spring.auditlog

import com.github.structlog4j.IToLog

data class LogEntry(
        private val currentUserId: String,
        private val companyId: String,
        private val teamId: String,
        private val authorization: String,
        private val targetType: String,
        private val targetId: String,
        private val originalContents: String,
        private val updatedContents: String
) : IToLog {
    override fun toLog(): Array<Any> {
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