import api.JiraClient
import io.ktor.client.*
import kotlinx.datetime.Instant

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun jiraInstant(date: String): Instant

expect fun client(): JiraClient