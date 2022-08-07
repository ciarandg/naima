/**
 * Wrapper around whatever dbms we use (currently MongoDB)
 */

import com.mongodb.client.MongoCollection
import org.litote.kmongo.KMongo
import org.litote.kmongo.aggregate
import org.litote.kmongo.ascending
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import org.litote.kmongo.`in`
import org.litote.kmongo.inc
import org.litote.kmongo.match
import org.litote.kmongo.setValue
import org.litote.kmongo.sort
import system.data.Suggestion

class Database {
    private val client = KMongo.createClient(Environment.mongoConnString)
    private val database = client.getDatabase("suggestions")
    private val collection: MongoCollection<Suggestion> = database.getCollection()

    fun insert(suggestion: Suggestion) {
        collection.insertOne(suggestion)
    }

    fun getSuggestionsRanked(): List<Suggestion> = collection.aggregate<Suggestion>(
        match(Suggestion::hasBeenChosen eq false),
        sort(ascending(Suggestion::timesVotable))
    ).toList()

    fun incrementTimesVotable(suggestions: Collection<Suggestion>) {
        collection.updateMany(
            Suggestion::releaseGroup `in` suggestions.map { it.releaseGroup },
            inc(Suggestion::timesVotable, 1)
        )
    }

    fun incrementTimesVoted(suggestion: Suggestion, inc: Int) {
        collection.updateOne(
            Suggestion::releaseGroup eq suggestion.releaseGroup,
            inc(Suggestion::timesVoted, inc)
        )
    }

    fun markAsChosen(suggestion: Suggestion) {
        collection.updateOne(
            Suggestion::releaseGroup eq suggestion.releaseGroup,
            setValue(Suggestion::hasBeenChosen, true)
        )
    }
}
