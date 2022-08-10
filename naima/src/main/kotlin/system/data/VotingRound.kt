package system.data

data class VotingRound(
    val choices: List<Suggestion>,
    val voteChannelId: String,
    val voteMessageId: String
)
