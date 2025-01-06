package org.ptss.support.domain.constants

object SecurityMessages {
    // Invalid authentication messages
    const val UNAUTHORIZED_ACCESS = "Unauthorized access" // Generic message so that you don't give too much away to the user
    const val MISSING_GROUP = "Group association is required for this operation"
    const val TOKEN_EXPIRED = "Authentication token has expired"
    const val INVALID_USER_ID = "Authentication token has an invalid or missing user id"
}