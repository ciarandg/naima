package system.data

import jda
import net.dv8tion.jda.api.entities.Message

data class VotingRound(
    val choices: List<Suggestion>,
    val voteChannelId: String,
    val voteMessageId: String
) {
    fun fetchVotingMessage(): Message {
        val channel = jda.getTextChannelById(voteChannelId)
            ?: throw IllegalStateException("Voting channel must exist")
        return channel
            .retrieveMessageById(voteMessageId)
            .complete()
    }
}
