package org.ptss.support.domain.interfaces.services

import org.ptss.support.domain.templates.BaseEmailTemplate

interface IEmailService {
    suspend fun sendEmail(template: BaseEmailTemplate)
}
