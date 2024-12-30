package org.ptss.support.domain.config

import org.eclipse.microprofile.config.ConfigProvider

object InvitationProperties {
    val validityHours: Long
        get() = ConfigProvider.getConfig().getValue("app.invitation.validity.hours", Long::class.java)
}