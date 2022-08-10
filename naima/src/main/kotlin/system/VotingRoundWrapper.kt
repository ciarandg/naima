package system

import database
import jda
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.InteractionHook
import system.data.Suggestion
import system.data.VotingRound
import system.exception.NoAlbumsAvailableException
import java.lang.Integer.min

class VotingRoundWrapper(eventHook: InteractionHook) {
    private val choices: List<Suggestion> = run {
        val allSuggestions = database.getSuggestionsRanked()
        allSuggestions.take(min(ALBUMS_PER_ROUND, allSuggestions.size))
    }
    private val message: Message = eventHook.sendMessage(formatChoices()).complete()
    val round = VotingRound(
        choices,
        message.channel.id,
        message.id
    )

    init {
        database.incrementTimesVotable(round.choices)
    }

    fun fetchVotingMessage(): Message {
        val channel = jda.getTextChannelById(round.voteChannelId)
            ?: throw IllegalStateException("Voting channel must exist")
        return channel
            .retrieveMessageById(round.voteMessageId)
            .complete()
    }

    private fun formatChoices() =
        if (round.choices.isEmpty()) {
            throw NoAlbumsAvailableException()
        } else {
            round.choices.mapIndexed { i, pick ->
                "${i + 1}. ${pick.releaseGroup.prettyName}"
            }.reduce { acc, s ->
                acc + "\n" + s
            }
        }

    companion object {
        private const val ALBUMS_PER_ROUND = 5
    }
}
