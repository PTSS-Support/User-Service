package org.ptss.support.infrastructure.external_services.auth.dtos.requests

import org.ptss.support.api.dtos.requests.auth.*
import org.ptss.support.infrastructure.external_services.auth.dtos.requests.*

fun LoginRequest.toExternalDto() = AuthLoginRequest(
    email = this.email,
    password = this.password
)

fun PinCreateRequest.toExternalDto() = AuthCreatePinRequest(
    pin = this.pin
)

fun PinUpdateRequest.toExternalDto() = AuthUpdatePinRequest(
    oldPin = this.currentPin,
    newPin = this.newPin
)

fun PasswordUpdateRequest.toExternalDto() = AuthUpdatePasswordRequest(
    oldPassword = this.currentPassword,
    newPassword = this.newPassword
)