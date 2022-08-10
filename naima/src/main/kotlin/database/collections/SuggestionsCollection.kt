package database.collections

import com.mongodb.client.MongoCollection
import musicbrainz.data.ReleaseGroup
import org.litote.kmongo.aggregate
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.`in`
import org.litote.kmongo.inc
import org.litote.kmongo.match
import org.litote.kmongo.setValue
import system.data.Suggestion

class SuggestionsCollection(private val collection: MongoCollection<Suggestion>) {
    fun insert(suggestion: Suggestion) {
        collection.insertOne(suggestion)
    }

    fun getUnchosenRanked(): List<Suggestion> = collection.aggregate<Suggestion>(
        match(Suggestion::hasBeenChosen eq false)
    ).shuffled().sortedBy { it.timesVotable }

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

    fun getSuggestion(releaseGroup: ReleaseGroup): Suggestion? =
        collection.findOne(
            Suggestion::releaseGroup eq releaseGroup
        )
}
