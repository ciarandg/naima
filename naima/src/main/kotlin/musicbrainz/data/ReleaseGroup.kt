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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReleaseGroup

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
