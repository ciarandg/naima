package discord.embed

import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.io.File

class MultiAlbumCoverEmbed(releaseGroups: List<ReleaseGroup>) {
    private val outPath = run {
        val ids = releaseGroups.map { it.id }
        val combined = ids.reduce { acc, id -> "$acc-$id" }
        "/tmp/$combined.jpg"
    }

    private val imageMagickCommand = run {
        val imageUrls = releaseGroups.map { AlbumCoverEmbed(it).imageUrl }
        fun List<String>.spaceDelimit() = this.reduce { acc, s -> "$acc $s" }
        val urlsCombined = imageUrls.map { '"' + it + '"' }.spaceDelimit()
        listOf(
            "magick",
            urlsCombined,
            "-resize $ALBUM_COVER_WIDTH_PX",
            "-crop " + ALBUM_COVER_WIDTH_PX + "x" + ALBUM_COVER_WIDTH_PX,
            "+append $outPath"
        ).spaceDelimit()
    }


    private val fallbackImageUrl = "https://coverartarchive.org/release-group/a5572828-f7f2-405d-8853-a2019b019e07/front"
    val imageUrl: String by lazy {
        "TODO"
    }
    fun build(): MessageEmbed = TODO()

    private fun generateImage(): File? {
        val processBuilder = ProcessBuilder(imageMagickCommand)
        val process = processBuilder.start()
        process.waitFor()
        return File(outPath).let { if (it.isFile) it else null }
    }

    private fun uploadToObjectStorage() {}

    companion object {
        private const val ALBUM_COVER_WIDTH_PX = 500
    }
}