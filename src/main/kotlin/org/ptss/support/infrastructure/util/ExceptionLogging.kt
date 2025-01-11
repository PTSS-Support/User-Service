package org.ptss.support.infrastructure.util

import io.quarkus.logging.Log

suspend fun <T> executeWithExceptionLoggingAsync(
    operation: suspend () -> T,
    logMessage: String,
    exceptionHandling: ((Exception) -> Exception)? = null,
    vararg args: Any,
): T {
    return try {
        operation()
    } catch (ex: Exception) {
        Log.error(logMessage.format(*args), ex)
        throw exceptionHandling?.invoke(ex) ?: ex
    }
}