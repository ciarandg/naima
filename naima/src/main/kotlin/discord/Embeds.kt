package discord

import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.EmbedBuilder

object Embeds {
    fun albumCoverEmbed(releaseGroup: ReleaseGroup) = EmbedBuilder()
        .setImage("https://coverartarchive.org/release-group/${releaseGroup.id}/front")
        .build()
}
