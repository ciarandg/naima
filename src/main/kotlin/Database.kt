/**
 * Wrapper around whatever dbms we use (currently MongoDB)
 */

import musicbrainz.data.ReleaseGroup
import org.litote.kmongo.*

class Database {
    val client = KMongo.createClient()
    val database = client.getDatabase("suggestions")
    val collection = database.getCollection<ReleaseGroup>()

    fun insert(releaseGroup: ReleaseGroup) {
        collection.insertOne(releaseGroup)
    }
}
