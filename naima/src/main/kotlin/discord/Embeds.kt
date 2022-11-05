package discord

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.io.File
import java.nio.file.Path

object Embeds {
    private val client = HttpClient(CIO)

    private fun albumCoverUrl(releaseGroup: ReleaseGroup) =
        "https://coverartarchive.org/release-group/${releaseGroup.id}/front"

    fun albumCoverEmbed(releaseGroup: ReleaseGroup) = EmbedBuilder()
        .setImage(albumCoverUrl(releaseGroup))
        .build()

    private class AlbumCoverEntry(val releaseGroup: ReleaseGroup) {
        val localPath: Path = Path.of("./${releaseGroup.id}.jpg")
        val file: File = localPath.toFile()

        suspend fun fetchImageInBytes(): ByteArray? = run {
            val response = client.get(albumCoverUrl(releaseGroup))
            if (response.status != HttpStatusCode.OK) { null}
            else { response.body() }
        }
        suspend fun writeToFile() {
            fetchImageInBytes()?.let { file.writeBytes(it) }
        }
    }

    suspend fun multiAlbumCoverEmbed(releaseGroups: List<ReleaseGroup>): MessageEmbed {
        // for each rg, download images to a temp dir, keep a list of paths and nulls
        val entries = releaseGroups.map { AlbumCoverEntry(it) }
        entries.forEach { it.writeToFile() }

        // map nulls to a placeholder image path
        val imagePaths = entries.map {
            val placeHolderPath = Path.of("./album_cover_placeholder.jpg")
            println("Checking album ${it.releaseGroup.prettyName}")
            println("Does ${it.file.absolutePath} exist? ${it.file.isFile}")
            println()
            if (it.file.isFile) { it.localPath }
            else { placeHolderPath}
        }

        // feed paths into imagemagick script, spit file out into temp dir
        // push image to some kind of storage backend wit http access, link to image
        TODO()
    }
}
