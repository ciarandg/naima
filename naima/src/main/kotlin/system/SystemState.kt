package system

import database
import discord.Emojis
import net.dv8tion.jda.api.interactions.InteractionHook
import system.data.Suggestion
import system.exception.VotingRoundAlreadyOpenException
import system.exception.VotingRoundNotYetOpenException

object SystemState {
    private var currentVotingRound: VotingRoundWrapper? = null

    fun openVotingRound(eventHook: InteractionHook): VotingRoundWrapper = currentVotingRound?.let {
        throw VotingRoundAlreadyOpenException()
    } ?: run {
        val newRound = VotingRoundWrapper(eventHook)
        currentVotingRound = newRound
        return newRound
    }

    fun closeVotingRound(): Suggestion = currentVotingRound?.let { round ->
        val tally = getVoteTally(round)
        val winner = round.round.choices[tally.getWinner().emojiIndex]
        tally.forEach { database.incrementTimesVoted(round.round.choices[it.emojiIndex], it.voteCount) }
        database.markAsChosen(winner)
        currentVotingRound = null
        return winner
    } ?: throw VotingRoundNotYetOpenException()

    private fun getVoteTally(votingRound: VotingRoundWrapper): List<VoteTallyItem> {
        val reactions = votingRound.fetchVotingMessage().reactions
        return reactions
            .map { VoteTallyItem(Emojis.emojiToIndex(it.emoji), it.count - 1) }
            .filter { it.emojiIndex >= 0 && it.emojiIndex < votingRound.round.choices.size }
    }

    private fun List<VoteTallyItem>.getWinner(): VoteTallyItem {
        val byVoteCount = this.sortedByDescending { it.voteCount }
        val potentialWinners = byVoteCount.filter { it.voteCount == byVoteCount.first().voteCount }
        return potentialWinners.random()
    }

    private data class VoteTallyItem(val emojiIndex: Int, val voteCount: Int)
}
