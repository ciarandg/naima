package discord

import discord.embed.AlbumCoverEmbed
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.entities.MessageEmbed
import java.io.File

object Embeds {
    private val client = HttpClient(CIO)

    private class AlbumCoverEntry(val releaseGroup: ReleaseGroup) {
        val file: File = File("/tmp/album-cover-${releaseGroup.id}.jpg")

        suspend fun fetchImageInBytes(): ByteArray? = run {
            val response = client.get(AlbumCoverEmbed(releaseGroup).imageUrl)
            if (response.status != HttpStatusCode.OK) { null } else { response.body() }
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
            val placeHolderPath = ClassLoader.getSystemResource("album_cover_placeholder.jpg").path
            println("Placeholder ${File(placeHolderPath).absolutePath} is file? ${File(placeHolderPath).isFile}")
            println("Checking album ${it.releaseGroup.prettyName}")
            println("Does ${it.file.absolutePath} exist? ${it.file.isFile}")
            println()
            if (it.file.isFile) { it.file.path } else { placeHolderPath }
        }

        // feed paths into imagemagick script, spit file out into temp dir
        // push image to some kind of storage backend wit http access, link to image
        TODO()
    }
}
