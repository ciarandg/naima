package discord.embed

import kotlinx.coroutines.runBlocking
import musicbrainz.MusicBrainz
import musicbrainz.MusicBrainz.searchForReleaseGroup
import musicbrainz.data.ReleaseGroup
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MultiAlbumCoverEmbedTest {
    private val validReleaseGroups: List<ReleaseGroup> = runBlocking {
        listOf(
            searchForReleaseGroup("London Calling", "The Clash"),
            searchForReleaseGroup("Doolittle", "Pixies"),
            searchForReleaseGroup("Dummy", "Portishead")
        ).map { it!! }
    }

    @Test
    fun build() {
        println("Building an embed with these release groups:")
        println(validReleaseGroups)
        val embed = MultiAlbumCoverEmbed(validReleaseGroups).build()
        println("Resulting URL:")
        println(embed.image!!.url!!)
    }
}