package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.requests.forgot_password.RequestPasswordResetRequest
import org.ptss.support.api.dtos.requests.forgot_password.PasswordResetVerificationRequest
import org.ptss.support.api.dtos.requests.forgot_password.ResetPasswordRequest
import org.ptss.support.domain.commands.forgot_password.RequestPasswordResetCommand
import org.ptss.support.domain.commands.forgot_password.ResetPasswordCommand
import org.ptss.support.domain.commands.forgot_password.VerifyPasswordResetCommand
import org.ptss.support.domain.interfaces.commands.forgot_password.IRequestPasswordResetCommandHandler
import org.ptss.support.domain.interfaces.commands.forgot_password.IResetPasswordCommandHandler
import org.ptss.support.domain.interfaces.commands.forgot_password.IVerifyPasswordResetCommandHandler
import org.ptss.support.domain.interfaces.facades.IForgotPasswordFacade
import org.ptss.support.domain.interfaces.services.IEmailService
import org.ptss.support.domain.templates.EmailTemplates
import io.quarkus.logging.Log
import org.ptss.support.api.dtos.requests.toCommand

@ApplicationScoped
class ForgotPasswordFacade(
    private val requestPasswordResetCommandHandler: IRequestPasswordResetCommandHandler,
    private val verifyPasswordResetCommandHandler: IVerifyPasswordResetCommandHandler,
    private val resetPasswordCommandHandler: IResetPasswordCommandHandler,
    private val emailService: IEmailService
) : IForgotPasswordFacade {

    override suspend fun requestPasswordReset(request: RequestPasswordResetRequest) {
        Log.info("Processing password reset request for email: ${request.email}")

        val resetCode = requestPasswordResetCommandHandler.handleAsync(request.toCommand())

        emailService.sendEmail(
            EmailTemplates.passwordResetEmail(
                email = request.email,
                resetCode = resetCode
            )
        )
    }

    override suspend fun verifyPasswordReset(request: PasswordResetVerificationRequest) {
        Log.info("Verifying password reset code for email: ${request.email}")

        verifyPasswordResetCommandHandler.handleAsync(request.toCommand())
    }

    override suspend fun resetPassword(request: ResetPasswordRequest) {
        Log.info("Processing password reset")

        resetPasswordCommandHandler.handleAsync(request.toCommand())
    }
}