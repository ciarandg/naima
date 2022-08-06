package musicbrainz.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ReleaseGroup(
    val id: String,
    val title: String,
    @SerialName("count") val releaseCount: Int,
    @SerialName("first-release-date") val releaseDate: String,
    @SerialName("artist-credit") val artistCredit: List<ArtistCredit>
) {
    val artistName
        get() = artistCredit[0].name
    val prettyName
        get() = "$artistName - $title"

    @kotlinx.serialization.Serializable
    data class ArtistCredit(
        val name: String
    )
}
