package system

import net.dv8tion.jda.api.entities.Message
import system.data.Suggestion
import Emojis

object SystemState {
    private var currentVotingRound: VotingRound? = null
    var votingMessage: Message? = null

    fun openVotingRound(): VotingRound = currentVotingRound?.let {
        throw IllegalStateException("Can't open a voting round when one is already open")
    } ?: run {
        val newRound = VotingRound()
        currentVotingRound = newRound
        return newRound
    }

    fun closeVotingRound(): Suggestion = currentVotingRound?.let { round ->
        // Update message from history
        val channel = votingMessage?.channel
        votingMessage = channel?.retrieveMessageById(votingMessage?.id!!)?.complete()

        val reactions = votingMessage?.reactions
        println(reactions)
        // TODO handle reactions being null
        val voteCounts = reactions!!
            .map { reaction -> Pair(reaction.emoji, reaction.count) }
            .map { (emoji, count) -> Pair(Emojis.emojiToIndex(emoji), count) }
            .filter { (emojiIndex, _) -> emojiIndex >= 0 &&  emojiIndex < round.choices.size }
        println(voteCounts)
        val winnerIndex: Int = voteCounts.maxByOrNull { it.second }?.first!!
        val winner = round.choices[winnerIndex]
        println(winner)

        // TODO update timesVoted in database

        currentVotingRound = null
        return winner
    } ?: throw IllegalStateException("Can't close a voting round when one isn't open")
}
