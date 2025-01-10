package org.ptss.support.domain.templates

import org.ptss.support.domain.config.InvitationProperties
import org.ptss.support.domain.models.EmailTemplate

// Kept in one object for now since we only have two templates
object EmailTemplates {
    fun invitationEmail(email: String, verificationCode: String) = EmailTemplate(
        to = email,
        subject = "Uw uitnodiging tot PTSS Support",
        content = """
            <h1>Welkom bij PTSS Support!</h1>
    
            <p>Je bent uitgenodigd om deel te nemen aan ons platform. Om je registratie te voltooien, 
            gebruik de volgende verificatiecode:</p>
            
            <div class="code-box">
                <strong>$verificationCode</strong>
            </div>
            
            <p>Deze code verloopt over <strong>${InvitationProperties.validityHours} uur.</strong></p>
            
            <p>Met vriendelijke groet,<br>
            PTSS Support Team</p>
        """.trimIndent()
    )

    fun passwordResetEmail(email: String, resetCode: String) = EmailTemplate(
        to = email,
        subject = "Password Reset Request",
        content = """
            <h1>Wachtwoord Herstellen</h1>
        
            <p>We hebben een verzoek ontvangen om je wachtwoord te herstellen. Gebruik de volgende code
            om verder te gaan met het herstellen van je wachtwoord:</p>
            
            <div class="code-box">
                <strong>$resetCode</strong>
            </div>
            
            <p><strong>Als je geen herstelverzoek hebt ingediend</strong>, kun je deze e-mail negeren.</p>
            
            <p>Met vriendelijke groet,<br>
            PTSS Support Team</p>
        """.trimIndent()
    )
}