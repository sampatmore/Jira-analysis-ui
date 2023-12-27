import api.JiraClient
import io.ktor.client.*
import kotlinx.datetime.*

/**
 * Badly written utility function to get Instant from a jira timestamp
 * example 2023-12-22T13:50:22.256+0000
 */
actual fun jiraInstant(date: String): Instant {
    return date.run {
        LocalDateTime(
            date = LocalDate(
                year = substring(0..3).toInt(),
                monthNumber = substring(5..6).toInt(),
                dayOfMonth = substring(7..9).toInt(),
            ),
            time = LocalTime(
                hour = substring(11..13).toInt(),
                minute = substring(13..14).toInt(),
                second = substring(15..16).toInt(),
                nanosecond = substring(17..19).toInt(),
            )
        ).toInstant(TimeZone.UTC)
    }
}

actual fun client(): JiraClient {
    TODO("Not yet implemented")
}