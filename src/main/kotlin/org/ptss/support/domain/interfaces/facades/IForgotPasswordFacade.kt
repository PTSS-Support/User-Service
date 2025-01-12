package org.ptss.support.domain.interfaces.facades

import org.ptss.support.api.dtos.requests.forgot_password.PasswordResetVerificationRequest
import org.ptss.support.api.dtos.requests.forgot_password.RequestPasswordResetRequest
import org.ptss.support.api.dtos.requests.forgot_password.ResetPasswordRequest

interface IForgotPasswordFacade {
    suspend fun requestPasswordReset(request: RequestPasswordResetRequest)
    suspend fun verifyPasswordReset(request: PasswordResetVerificationRequest)
    suspend fun resetPassword(request: ResetPasswordRequest)
}