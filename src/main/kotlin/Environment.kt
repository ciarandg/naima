object Environment {
    val botToken: String = System.getenv("BOT_TOKEN")
    val mongoUser: String = System.getenv("MONGO_INITDB_ROOT_USERNAME")
    val mongoPassword: String = System.getenv("MONGO_INITDB_ROOT_PASSWORD")
    val mongoHost: String = System.getenv("MONGO_HOST")
    val mongoConnString: String = "mongodb://$mongoUser:$mongoPassword@$mongoHost"

    object S3Credentials {
        val accessKey = System.getenv("S3_ACCESS_KEY")
        val secretKey = System.getenv("S3_SECRET_KEY")
        val endpoint = System.getenv("S3_ENDPOINT")
        val bucket = System.getenv("S3_BUCKET")
    }
}
