package org.ptss.support.infrastructure.services

import io.quarkus.mailer.Mail
import io.quarkus.logging.Log
import io.quarkus.mailer.reactive.ReactiveMailer
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.services.IEmailService
import org.ptss.support.domain.templates.BaseEmailTemplate
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync

@ApplicationScoped
class EmailService(
    private val reactiveMailer: ReactiveMailer
) : IEmailService {
    override suspend fun sendEmail(template: BaseEmailTemplate) =
        executeWithExceptionLoggingAsync(
            operation = {
                Log.info("Creating email with subject: ${template.subject}")
                val mail = Mail.withHtml(
                    template.to,
                    template.subject,
                    template.wrappedContent
                )

                reactiveMailer.send(mail).awaitSuspending()
                Log.info("Email sent successfully to: ${template.to}")
            },
            logMessage = "Failed to send email to: ${template.to}"
        )
}
