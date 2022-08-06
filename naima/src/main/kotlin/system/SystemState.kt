package system

import system.data.Suggestion

object SystemState {
    private val suggestionPool = SuggestionPool()
    private var currentVotingRound: VotingRound? = null

    fun openVotingRound(): VotingRound = currentVotingRound?.let {
        throw IllegalStateException("Can't open a voting round when one is already open")
    } ?: run {
        val newRound = VotingRound()
        currentVotingRound = newRound
        return newRound
    }

    fun closeVotingRound(): Suggestion = currentVotingRound?.let { round ->
        currentVotingRound = null
        val winner = round.choices.random()
        val others = round.choices.minus(winner)
        // TODO resubmit others to suggestion pool as previously suggested
        return winner
    } ?: throw IllegalStateException("Can't close a voting round when one isn't open")
}
