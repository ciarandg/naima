package discord.embed

import kotlinx.coroutines.runBlocking
import musicbrainz.MusicBrainz.searchForReleaseGroup
import musicbrainz.data.ReleaseGroup
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MultiAlbumCoverEmbedTest {
    private val validReleaseGroupsWithCoverArt: List<ReleaseGroup> = runBlocking {
        listOf(
            searchForReleaseGroup("London Calling", "The Clash"),
            searchForReleaseGroup("Doolittle", "Pixies"),
            searchForReleaseGroup("Dummy", "Portishead")
        ).map { it!! }
    }

    private val validReleaseGroupsMissingCoverArt: List<ReleaseGroup> = runBlocking {
        listOf(
            searchForReleaseGroup("Chet Baker Sings", "Chet Baker")
        ).map { it!! }
    }

    @Test
    fun buildBestCase() {
        println("Building an embed with these release groups:")
        println(validReleaseGroupsWithCoverArt)
        val embed = MultiAlbumCoverEmbed(validReleaseGroupsWithCoverArt).build()
        println("Resulting URL:")
        println(embed!!.image!!.url!!)
    }

    @Test
    fun buildWithMissingCoverArt() {
        val releaseGroups = validReleaseGroupsWithCoverArt.plus(validReleaseGroupsMissingCoverArt)
        println("Building an embed with these release groups:")
        println(releaseGroups)
        val embed = MultiAlbumCoverEmbed(releaseGroups).build()
        assertEquals(embed, null)
    }
}
