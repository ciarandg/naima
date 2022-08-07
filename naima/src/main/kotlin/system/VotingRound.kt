package system

import database
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.InteractionHook
import system.data.Suggestion

class VotingRound(eventHook: InteractionHook) {
    val choices: List<Suggestion> = run {
        val allSuggestions = database.getSuggestionsRanked()
        allSuggestions.take(ALBUMS_PER_ROUND)
    }
    private val message: Message = eventHook.sendMessage(formatChoices()).complete()

    init {
        database.incrementTimesVotable(choices)
    }

    fun fetchVotingMessage(): Message =
        message.channel.retrieveMessageById(message.id).complete()

    private fun formatChoices() =
        if (choices.isEmpty()) {
            "No albums available for vote"
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
