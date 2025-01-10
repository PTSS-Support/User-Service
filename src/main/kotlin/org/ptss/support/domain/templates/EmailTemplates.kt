package org.ptss.support.domain.templates

import org.ptss.support.domain.config.InvitationProperties
import org.ptss.support.domain.models.EmailTemplate

// Kept in one object for now since we only have two templates
object EmailTemplates {
    fun invitationEmail(email: String, verificationCode: String) = EmailTemplate(
        to = email,
        subject = "Your Invitation to PTSS Support",
        content = """
            <h1>Welcome to PTSS Support!</h1>
            
            <p>You have been invited to join our platform. To complete your registration, 
            please use the following verification code:</p>
            
            <div style="background-color: #f5f5f5; padding: 15px; margin: 20px 0; text-align: center; font-size: 24px;">
                <strong>$verificationCode</strong>
            </div>
            
            <p>This code will expire in ${InvitationProperties.validityHours} hours.</p>
            
            <p>Best regards,<br>
            PTSS Support Team</p>
        """.trimIndent()
    )

    fun passwordResetEmail(email: String, resetCode: String) = EmailTemplate(
        to = email,
        subject = "Password Reset Request",
        content = """
            <h1>Password Reset Request</h1>
            
            <p>We received a request to reset your password. Use the following code
            to proceed with your password reset:</p>
            
            <div style="background-color: #f5f5f5; padding: 15px; margin: 20px 0; text-align: center; font-size: 24px;">
                <strong>$resetCode</strong>
            </div>
            
            <p>If you didn't request this reset, please ignore this email.</p>
            
            <p>Best regards,<br>
            PTSS Support Team</p>
        """.trimIndent()
    )
}