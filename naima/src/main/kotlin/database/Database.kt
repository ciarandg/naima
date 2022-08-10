package database

/**
 * Wrapper around whatever dbms we use (currently MongoDB)
 */

import Environment
import database.collections.SuggestionsCollection
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

class Database {
    private val client = KMongo.createClient(Environment.mongoConnString)
    private val database = client.getDatabase("naima")
    val suggestions = SuggestionsCollection(database.getCollection())
}
