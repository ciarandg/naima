package discord.embed

import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.io.File

class MultiAlbumCoverEmbed(releaseGroups: List<ReleaseGroup>) {
    private val fallbackImageUrl = "https://coverartarchive.org/release-group/a5572828-f7f2-405d-8853-a2019b019e07/front"
    val imageUrl: String by lazy {
        "TODO"
    }
    fun build(): MessageEmbed = TODO()

    private fun generateImage(): File? {
        val outPath = "/tmp/multi.jpg"
        val processBuilder = ProcessBuilder("magick \"$fallbackImageUrl\" -resize 1000 $outPath")
        val process = processBuilder.start()
        process.waitFor()
        return File(outPath).let { if (it.isFile) it else null }
    }

    private fun uploadToObjectStorage() {}
}