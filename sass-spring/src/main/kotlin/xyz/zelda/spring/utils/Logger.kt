package xyz.zelda.spring.utils

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory

inline fun <reified T> T.loggerFor(): ILogger {
    return SLoggerFactory.getLogger(T::class.java)
}