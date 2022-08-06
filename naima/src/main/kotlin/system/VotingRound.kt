package system

import database
import system.data.Suggestion

class VotingRound {
    val choices: Set<Suggestion> = run {
        val allSuggestions = database.getSuggestionsRanked()
        allSuggestions.take(ALBUMS_PER_ROUND).toSet()
    }

    companion object {
        private const val ALBUMS_PER_ROUND = 3
    }
}
