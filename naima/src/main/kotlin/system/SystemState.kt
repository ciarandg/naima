package system

import system.data.Suggestion
import Emojis
import database
import net.dv8tion.jda.api.interactions.InteractionHook

object SystemState {
    private var currentVotingRound: VotingRound? = null

    fun openVotingRound(eventHook: InteractionHook): VotingRound = currentVotingRound?.let {
        throw IllegalStateException("Can't open a voting round when one is already open")
    } ?: run {
        val newRound = VotingRound(eventHook)
        currentVotingRound = newRound
        return newRound
    }

    fun closeVotingRound(): Suggestion = currentVotingRound?.let { round ->
        val tally = getVoteTally(round)
        val winner = round.choices[tally.getWinner().emojiIndex]
        tally.forEach { database.incrementTimesVoted(round.choices[it.emojiIndex], it.voteCount) }
        currentVotingRound = null
        return winner
    } ?: throw IllegalStateException("Can't close a voting round when one isn't open")

    private fun getVoteTally(votingRound: VotingRound): List<VoteTallyItem> {
        val reactions = votingRound.fetchVotingMessage().reactions
        return reactions
            .map { VoteTallyItem(Emojis.emojiToIndex(it.emoji), it.count - 1) }
            .filter { it.emojiIndex >= 0 && it.emojiIndex < votingRound.choices.size }
    }

    private fun List<VoteTallyItem>.getWinner(): VoteTallyItem {
        val byVoteCount = this.sortedByDescending { it.voteCount }
        val potentialWinners = byVoteCount.filter { it.voteCount == byVoteCount.first().voteCount }
        return potentialWinners.random()
    }

    private data class VoteTallyItem(val emojiIndex: Int, val voteCount: Int)
}
