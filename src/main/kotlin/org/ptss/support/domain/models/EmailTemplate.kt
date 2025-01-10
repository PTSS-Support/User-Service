package org.ptss.support.domain.models

data class EmailTemplate(
    val to: String,
    val subject: String,
    val content: String,
    val isHtml: Boolean = true
)