package musicbrainz.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ReleaseGroup(
    val id: String,
    val title: String,
    @SerialName("count") val releaseCount: Int,
    @SerialName("first-release-date") val releaseDate: String
)
