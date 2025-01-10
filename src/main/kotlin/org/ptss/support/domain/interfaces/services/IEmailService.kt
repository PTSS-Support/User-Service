package org.ptss.support.domain.interfaces.services

import org.ptss.support.domain.models.EmailTemplate

interface IEmailService {
    suspend fun sendEmail(template: EmailTemplate)
}
