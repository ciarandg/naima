package database.collections

import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.set
import org.litote.kmongo.setTo
import system.data.Suggestion
import system.data.VotingRound

class VotingRoundsCollection(
    private val collection: MongoCollection<VotingRound>
) : MongoCollection<VotingRound> by collection {
    fun insert(votingRound: VotingRound) {
        collection.insertOne(votingRound)
    }

    fun getOpenRound(): VotingRound? = collection.findOne(VotingRound::winner eq null)

    fun markAsClosed(votingRound: VotingRound, winner: Suggestion) {
        collection.updateOne(
            VotingRound::voteMessageId eq votingRound.voteMessageId,
            set(VotingRound::winner setTo winner)
        )
    }
}
