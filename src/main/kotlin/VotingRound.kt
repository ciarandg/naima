class VotingRound(suggestionPool: SuggestionPool) {
    val choices = suggestionPool.poll(ALBUMS_PER_ROUND)

    companion object {
        private const val ALBUMS_PER_ROUND = 3
    }
}
