package system

import database
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.InteractionHook
import system.data.Suggestion
import system.data.VotingRound
import java.lang.Integer.min

class VotingRoundWrapper(eventHook: InteractionHook) {
    private val choices: List<Suggestion> = run {
        val allSuggestions = database.suggestions.getUnchosenRanked()
        allSuggestions.take(min(ALBUMS_PER_ROUND, allSuggestions.size))
    }

    private val message: Message = eventHook.sendMessage(
        VotingRound.formatChoices(choices)
    ).complete()

    val round = VotingRound(
        choices,
        message.channel.id,
        message.id,
        true
    )

    init {
        database.suggestions.incrementTimesVotable(round.choices)
    }

    companion object {
        private const val ALBUMS_PER_ROUND = 5
    }
}
