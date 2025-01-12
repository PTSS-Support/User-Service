package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.ptss.support.api.dtos.requests.forgot_password.PasswordResetVerificationRequest
import org.ptss.support.api.dtos.requests.forgot_password.RequestPasswordResetRequest
import org.ptss.support.api.dtos.requests.forgot_password.ResetPasswordRequest
import org.ptss.support.domain.interfaces.controllers.IForgotPasswordController
import org.ptss.support.domain.interfaces.facades.IForgotPasswordFacade

@ApplicationScoped
class ForgotPasswordController @Inject constructor(
    private val forgotPasswordFacade: IForgotPasswordFacade
) : IForgotPasswordController {

    override suspend fun requestPasswordReset(request: RequestPasswordResetRequest): Response {
        forgotPasswordFacade.requestPasswordReset(request)
        return Response.noContent().build()
    }

    override suspend fun verifyPasswordReset(request: PasswordResetVerificationRequest): Response {
        forgotPasswordFacade.verifyPasswordReset(request)
        return Response.noContent().build()
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): Response {
        forgotPasswordFacade.resetPassword(request)
        return Response.noContent().build()
    }
}