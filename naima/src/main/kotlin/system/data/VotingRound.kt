package system.data

import jda
import net.dv8tion.jda.api.entities.Message
import system.exception.NoAlbumsAvailableException

data class VotingRound(
    val choices: List<Suggestion>,
    val voteChannelId: String,
    val voteMessageId: String,
    val winner: Suggestion? = null
) {
    fun fetchVotingMessage(): Message {
        val channel = jda.getTextChannelById(voteChannelId)
            ?: throw IllegalStateException("Voting channel must exist")
        return channel
            .retrieveMessageById(voteMessageId)
            .complete()
    }

    companion object {
        fun formatChoices(choices: List<Suggestion>) =
            if (choices.isEmpty()) {
                throw NoAlbumsAvailableException()
            } else {
                choices.mapIndexed { i, pick ->
                    "${i + 1}. ${pick.releaseGroup.prettyName}"
                }.reduce { acc, s ->
                    acc + "\n" + s
                }
            }
    }
}
