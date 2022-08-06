/**
 * Wrapper around whatever dbms we use (currently MongoDB)
 */

import com.mongodb.client.AggregateIterable
import com.mongodb.client.MongoCollection
import musicbrainz.data.ReleaseGroup
import org.litote.kmongo.*
import system.data.Suggestion

class Database {
    val client = KMongo.createClient(Environment.mongoConnString)
    val database = client.getDatabase("suggestions")
    val collection: MongoCollection<Suggestion> = database.getCollection()

    fun insert(suggestion: Suggestion) {
        collection.insertOne(suggestion)
    }

    fun getSuggestionsRanked(): List<Suggestion> = collection.aggregate<Suggestion>(
            match(Suggestion::hasBeenChosen eq false),
            sort(ascending(Suggestion::timesVotable))
        ).toList()
}
