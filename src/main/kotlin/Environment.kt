object Environment {
    val botToken: String = System.getenv("BOT_TOKEN")

    object ChannelIDs {
        val suggestionsChannel: String = System.getenv("SUGGESTIONS_CHANNEL_ID")
        val votingChannel: String = System.getenv("VOTING_CHANNEL_ID")
    }
}