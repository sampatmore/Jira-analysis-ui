package api.responses.changelog


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Author(
//    @SerialName("accountId")
//    val accountId: String,
//    @SerialName("displayName")
//    val displayName: String,
//    @SerialName("emailAddress")
//    val emailAddress: String?,
    @SerialName("self")
    val self: String,
    @SerialName("timeZone")
    val timeZone: String,
)