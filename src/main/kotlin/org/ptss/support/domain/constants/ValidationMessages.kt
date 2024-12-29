package org.ptss.support.domain.constants

import org.ptss.support.domain.constants.ValidationConstraints.EMAIL_MAX_LENGTH
import org.ptss.support.domain.constants.ValidationConstraints.NAME_MAX_LENGTH
import org.ptss.support.domain.constants.ValidationConstraints.VERIFICATION_CODE_LENGTH

// Constants for Validation Messages
object ValidationMessages {
    // Common
    private const val INVALID_NAME = "mag alleen letters, spaties, koppeltekens en apostroffen bevatten"
    private const val EMPTY_NAME = "mag niet leeg zijn"
    private const val NAME_LENGTH = "moet niet meer dan $NAME_MAX_LENGTH karakters zijn"

    // Email validation messages
    const val INVALID_EMAIL = "Vul een geldig email adres in"
    const val EMPTY_EMAIL = "Email $EMPTY_NAME"
    const val EMAIL_LENGTH = "moet niet meer dan $EMAIL_MAX_LENGTH karakters zijn"
    const val PATIENT_EMAIL_EMPTY = "Patiënt email $EMPTY_NAME"
    const val PATIENT_EMAIL_INVALID = "Patiënt email moet een geldig mailadres zijn"

    // Name validation messages
    const val EMPTY_FIRST_NAME = "Voornaam $EMPTY_NAME"
    const val EMPTY_LAST_NAME = "Achternaam $EMPTY_NAME"
    const val FIRST_NAME_LENGTH = "Voornaam $NAME_LENGTH"
    const val LAST_NAME_LENGTH = "Achternaam $NAME_LENGTH"
    const val FIRST_NAME_INVALID = "Voornaam $INVALID_NAME"
    const val LAST_NAME_INVALID = "Achternaam $INVALID_NAME"

    // Password validation messages
    const val EMPTY_PASSWORD = "Wachtwoord $EMPTY_NAME"
    const val INVALID_PASSWORD = "Wachtwoord moet minimaal 9 karakters bevatten, waaronder minimaal 1 kleine letter, " +
            "1 hoofdletter, 1 cijfer en 1 speciaal karakter"

    // Verification code validation messages
    const val EMPTY_VERIFICATION_CODE = "Uitnodigingscode $EMPTY_NAME"
    const val INVALID_VERIFICATION_CODE = "Uitnodigingscode moet exact $VERIFICATION_CODE_LENGTH cijfers bevatten"

    // Healthcare professional validation messages
    const val EMPTY_HEALTHCARE_PROFESSIONAL_ID = "Zorgverlener ID $EMPTY_NAME"
}