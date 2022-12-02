object Environment {
    val botToken: String = System.getenv("BOT_TOKEN")
    val mongoConnString: String = System.getenv("MONGODB_CONNSTRING")

    object ChannelIDs {
        val suggestionsChannel: String = System.getenv("SUGGESTIONS_CHANNEL_ID")
        val votingChannel: String = System.getenv("VOTING_CHANNEL_ID")
    }
}
