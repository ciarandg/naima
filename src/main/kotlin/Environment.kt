object Environment {
    val botToken: String = System.getenv("BOT_TOKEN")

    object MongoCredentials {
        val user: String = System.getenv("MONGO_INITDB_ROOT_USERNAME")
        val password: String = System.getenv("MONGO_INITDB_ROOT_PASSWORD")
        val host: String = System.getenv("MONGO_HOST")
        val connString: String = "mongodb://$user:$password@$host"
    }

    object S3Credentials {
        val accessKey = System.getenv("S3_ACCESS_KEY")
        val secretKey = System.getenv("S3_SECRET_KEY")
        val endpoint = System.getenv("S3_ENDPOINT")
        val bucket = System.getenv("S3_BUCKET")
    }
}
