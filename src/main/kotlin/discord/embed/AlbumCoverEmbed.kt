package discord.embed

import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

class AlbumCoverEmbed(releaseGroup: ReleaseGroup) {
    val imageUrl = "https://coverartarchive.org/release-group/${releaseGroup.id}/front"
    fun build(): MessageEmbed = EmbedBuilder()
        .setImage(imageUrl)
        .build()
}
