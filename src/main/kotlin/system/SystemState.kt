package system

import database
import discord.Emojis
import discord.embed.MultiAlbumCoverEmbed
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.interactions.InteractionHook
import system.data.Suggestion
import system.data.VotingRound
import system.exception.VotingRoundAlreadyOpenException
import system.exception.VotingRoundNotYetOpenException
import java.lang.Integer.min

object SystemState {
    private const val ALBUMS_PER_ROUND = 5

    fun openVotingRound(eventHook: InteractionHook): VotingRound = database.votingRounds.getOpenRound()?.let {
        throw VotingRoundAlreadyOpenException()
    } ?: run {
        val choices = getRoundChoices()
        database.suggestions.incrementTimesVotable(choices)
        val message = eventHook
            .sendMessage(VotingRound.formatChoices(choices))
            .addEmbeds(MultiAlbumCoverEmbed(choices.map { it.releaseGroup }).build())
            .complete()
        val round = VotingRound(
            choices,
            message.channel.id,
            message.id
        )
        database.votingRounds.insert(round)
        return database.votingRounds.getOpenRound() ?: throw IllegalStateException("There must be an open round")
    }

    private fun getRoundChoices(): List<Suggestion> {
        val allSuggestions = database.suggestions.getUnchosenRanked()
        return allSuggestions.take(min(ALBUMS_PER_ROUND, allSuggestions.size))
    }

    fun closeVotingRound(): Suggestion = database.votingRounds.getOpenRound()?.let { round ->
        val tally = getVoteTally(round)
        val winner = round.choices[tally.getWinner().emojiIndex]
        tally.forEach { database.suggestions.incrementTimesVoted(round.choices[it.emojiIndex], it.voteCount) }
        database.suggestions.markAsChosen(winner)
        database.votingRounds.markAsClosed(round, winner)
        return winner
    } ?: throw VotingRoundNotYetOpenException()

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
