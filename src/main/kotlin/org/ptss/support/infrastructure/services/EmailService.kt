package org.ptss.support.infrastructure.services

import io.quarkus.mailer.Mail
import io.quarkus.logging.Log
import io.quarkus.mailer.reactive.ReactiveMailer
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.services.IEmailService
import org.ptss.support.domain.models.EmailTemplate
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync

@ApplicationScoped
class EmailService(
    private val reactiveMailer: ReactiveMailer
) : IEmailService {
    override suspend fun sendEmail(template: EmailTemplate) =
        executeWithExceptionLoggingAsync(
            operation = {
                Log.info("Preparing to send email to: ${template.to}")
                val mail = if (template.isHtml) {
                    Log.info("Creating HTML email")
                    Mail.withHtml(
                        template.to,
                        template.subject,
                        wrapWithBaseTemplate(template.content)
                    )
                } else {
                    Log.info("Creating plain text email")
                    Mail.withText(
                        template.to,
                        template.subject,
                        template.content
                    )
                }

                Log.info("About to send email...")
                reactiveMailer.send(mail).await()
                Log.info("Email sent successfully to: ${template.to}")
            },
            logMessage = "Failed to send email to: ${template.to}"
        )

    private fun wrapWithBaseTemplate(content: String): String =
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    line-height: 1.6;
                    color: #333333;
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                }
                .footer {
                    margin-top: 30px;
                    padding-top: 20px;
                    border-top: 1px solid #eeeeee;
                    font-size: 12px;
                    color: #666666;
                }
            </style>
        </head>
        <body>
            $content
            <div class="footer">
                This is an automated message, please do not reply directly to this email.
                If you need assistance, please contact our support team.
            </div>
        </body>
        </html>
        """.trimIndent()
}
