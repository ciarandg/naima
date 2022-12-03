package event.handler

import database
import discord.embed.AlbumCoverEmbed
import kotlinx.coroutines.runBlocking
import musicbrainz.MusicBrainz
import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import system.data.Suggestion

class SuggestCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    private fun handleReleaseGroup(releaseGroup: ReleaseGroup) {
        database.suggestions.getSuggestion(releaseGroup)?.let { suggestion ->
            val ending =
                if (suggestion.hasBeenChosen) "already been suggested and chosen in a vote"
                else "already been suggested, but has not yet been chosen in a vote"
            event.hook.sendMessage(
                "Suggestion failed: ${suggestion.releaseGroup.prettyName} has $ending"
            ).queue()
        } ?: run {
            val requesterName = event.user.name
            val suggestion = Suggestion(
                releaseGroup,
                event.user.id,
                event.timeCreated.toInstant()
            )
            event.hook
                .sendMessage("$requesterName has requested ${releaseGroup.prettyName}")
                .addEmbeds(AlbumCoverEmbed(releaseGroup).build())
                .queue()
            database.suggestions.insert(suggestion)
        }
    }

    private fun handleSearchFailure() {
        event.hook
            .sendMessage("Couldn't find a match for the suggested album in the Musicbrainz database. Did you make a typo?")
            .queue()
    }

    override fun handle() {
        event.deferReply().queue()
        // TODO check if release group has already been suggested
        val artist = event.getOption("artist")!!.asString // TODO proper error handling
        val album = event.getOption("album")!!.asString // TODO proper error handling
        val releaseGroup = runBlocking { MusicBrainz.searchForReleaseGroup(album, artist) }
        releaseGroup?.let { handleReleaseGroup(releaseGroup) } ?: handleSearchFailure()
    }
}
