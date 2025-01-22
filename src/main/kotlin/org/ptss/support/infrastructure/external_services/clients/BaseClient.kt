package org.ptss.support.infrastructure.external_services.clients

import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.rest.client.RestClientBuilder
import java.net.URI

abstract class BaseClient(private val baseUrl: String) {
    protected fun getBaseUrl() = baseUrl

    protected inline fun <reified T> getClient(): T =
        RestClientBuilder.newBuilder()
            .baseUri(URI(getBaseUrl()))
            .build(T::class.java)

    protected suspend fun <T> executeRequest(
        responseClass: Class<T>,
        block: suspend () -> Response
    ): T = block().use { response ->
        when (response.statusInfo.family) {
            Response.Status.Family.SUCCESSFUL -> response.readEntity(responseClass)
            else -> throw RuntimeException("Request failed with status: ${response.status}")
        }
    }
}