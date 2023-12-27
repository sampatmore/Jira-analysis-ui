import api.JiraClient
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual fun jiraInstant(date: String): Instant {
    return LocalDateTime.parse(
        /* text = */ date,
        /* formatter = */ DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSZ")
    ).toKotlinLocalDateTime()
        .toInstant(TimeZone.UTC)
}

actual fun client(): JiraClient {
    val username: String = System.getenv("username") ?: throw IllegalArgumentException("Missing environment variable -username")
    val token: String = System.getenv("token") ?: throw IllegalArgumentException("Missing environment variable -token")

    val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    return JiraClient(httpClient, username, token)
}