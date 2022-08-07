package system

import system.data.Suggestion
import Emojis
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
        val reactions = round.fetchVotingMessage().reactions
        val voteCounts = reactions
            .map { VoteTallyEntry(Emojis.emojiToIndex(it.emoji), it.count) }
            .filter { it.emojiIndex >= 0 && it.emojiIndex < round.choices.size }
        val winnerIndex = run {
            val byVoteCount = voteCounts.sortedByDescending { it.voteCount }
            val potentialWinners = byVoteCount.filter { it.voteCount == byVoteCount.first().voteCount }
            potentialWinners.random().emojiIndex
        }
        val winner = round.choices[winnerIndex]

        // TODO update timesVoted in database

        currentVotingRound = null
        return winner
    } ?: throw IllegalStateException("Can't close a voting round when one isn't open")

    private data class VoteTallyEntry(val emojiIndex: Int, val voteCount: Int)
}
