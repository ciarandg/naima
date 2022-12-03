object Environment {
    val botToken: String = System.getenv("BOT_TOKEN")
    val mongoUser: String = System.getenv("MONGODB_INITDB_ROOT_USERNAME")
    val mongoPassword: String = System.getenv("MONGODB_INITDB_ROOT_PASSWORD")
    val mongoHost: String = System.getenv("MONGODB_HOST")
    val mongoConnString: String = "mongodb://${mongoUser}:${mongoPassword}@${mongoHost}"

    object ChannelIDs {
        val suggestionsChannel: String = System.getenv("SUGGESTIONS_CHANNEL_ID")
        val votingChannel: String = System.getenv("VOTING_CHANNEL_ID")
    }
}
