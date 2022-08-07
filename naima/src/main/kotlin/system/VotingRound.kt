package system

import database
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.InteractionHook
import system.data.Suggestion
import system.exception.NoAlbumsAvailableException
import java.lang.Integer.min

class VotingRound(eventHook: InteractionHook) {
    val choices: List<Suggestion> = run {
        val allSuggestions = database.getSuggestionsRanked()
        allSuggestions.take(min(ALBUMS_PER_ROUND, allSuggestions.size))
    }
    private val message: Message = eventHook.sendMessage(formatChoices()).complete()

    init {
        database.incrementTimesVotable(choices)
    }

    fun fetchVotingMessage(): Message =
        message.channel.retrieveMessageById(message.id).complete()

    private fun formatChoices() =
        if (choices.isEmpty()) {
            throw NoAlbumsAvailableException()
        } else {
            choices.mapIndexed { i, pick ->
                "${i + 1}. ${pick.releaseGroup.prettyName}"
            }.reduce { acc, s ->
                acc + "\n" + s
            }
        }

    companion object {
        private const val ALBUMS_PER_ROUND = 3
    }
}
