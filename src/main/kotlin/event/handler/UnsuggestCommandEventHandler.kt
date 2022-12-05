package event.handler

import database
import kotlinx.coroutines.runBlocking
import musicbrainz.MusicBrainz
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class UnsuggestCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    override fun handle() {
        event.deferReply().queue()
        val artist = event.getOption("artist")!!.asString // TODO proper error handling
        val album = event.getOption("album")!!.asString // TODO proper error handling
        val releaseGroup = runBlocking { MusicBrainz.searchForReleaseGroup(album, artist) }
        val message = releaseGroup?.let {
            val suggestion = database.suggestions.getSuggestion(releaseGroup)
            suggestion?.let {
                if (!suggestion.hasBeenChosen) {
                    database.suggestions.remove(suggestion)
                    "Successfully removed ${suggestion.releaseGroup.prettyName} from database"
                } else {
                    "${suggestion.releaseGroup.prettyName} has already been selected in a vote, and cannot be unsuggested"
                }
            } ?: "There is no prior suggestion in the database for this release group"
        } ?: "Couldn't find a match for the suggested album in the Musicbrainz database. Did you make a typo?"
        event.hook.sendMessage(message).queue()
    }
}
