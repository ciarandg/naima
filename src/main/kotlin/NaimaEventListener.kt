import kotlinx.coroutines.runBlocking
import musicbrainz.MusicBrainz
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class NaimaEventListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Discord API is ready!")
        jda.updateCommands().addCommands(
            NaimaCommands.submitCommand
        ).complete()
        println("Updated commands!")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name == NaimaCommands.submitCommand.name) {
            event.deferReply().queue()
            val requester = event.user.name
            val artist = event.getOption("artist")!!.asString
            val album = event.getOption("album")!!.asString
            val releaseGroup = runBlocking { MusicBrainz.searchForReleaseGroup(album, artist)   }
            val coverEmbed = EmbedBuilder()
                .setImage("https://coverartarchive.org/release-group/${releaseGroup.id}/front")
                .build()
            event.hook
                .sendMessage("$requester has requested `$album` by `$artist`")
                .addEmbeds(coverEmbed)
                .queue()
        }
    }
}