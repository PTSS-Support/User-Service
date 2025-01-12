package org.ptss.support.domain.config

import org.eclipse.microprofile.config.ConfigProvider

object ForgotPasswordProperties {
    val validityHours: Long
        get() = ConfigProvider.getConfig().getValue("app.forgot_password.validity.minutes", Long::class.java)
}