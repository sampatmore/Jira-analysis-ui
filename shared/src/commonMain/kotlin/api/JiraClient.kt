package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.core.*

class JiraClient(
    val httpClient: HttpClient,
    val username: String,
    val token: String,
) : Closeable {

    suspend inline fun <reified T> get(path: String): T {
        val response = httpClient.get {
            method = HttpMethod.Get
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            basicAuth(username = username, password = token)
            url("https://soenergy.atlassian.net/rest/api/3/$path")
        }

        return response.body<T>()
    }

    override fun close() = httpClient.close()
}