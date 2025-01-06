package org.ptss.support.security

import jakarta.ws.rs.NameBinding
import org.ptss.support.domain.constants.SecurityMessages.UNAUTHORIZED_ACCESS
import org.ptss.support.domain.enums.Role
import java.lang.annotation.Inherited

@Inherited
@NameBinding
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authentication(
    val roles: Array<Role>,
    val message: String = UNAUTHORIZED_ACCESS
)
