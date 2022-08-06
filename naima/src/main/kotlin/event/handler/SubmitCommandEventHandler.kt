package event.handler

import Database
import kotlinx.coroutines.runBlocking
import musicbrainz.MusicBrainz
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class SubmitCommandEventHandler(event: SlashCommandInteractionEvent, val database: Database) : EventHandler(event) {
    override fun handle() {
        event.deferReply().queue()
        val requester = event.user.name
        val artist = event.getOption("artist")!!.asString // TODO proper error handling
        val album = event.getOption("album")!!.asString // TODO proper error handling
        val releaseGroup = runBlocking { MusicBrainz.searchForReleaseGroup(album, artist) }
        val coverEmbed = EmbedBuilder()
            .setImage("https://coverartarchive.org/release-group/${releaseGroup.id}/front")
            .build()
        event.hook
            .sendMessage("$requester has requested `$album` by `$artist`")
            .addEmbeds(coverEmbed)
            .queue()
        database.insert(releaseGroup)
    }
}
