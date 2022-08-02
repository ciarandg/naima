package musicbrainz

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import musicbrainz.data.ReleaseGroup

object MusicBrainz {
    private const val RELEASE_GROUP_ENDPOINT = "https://musicbrainz.org/ws/2/release-group"
    private val client = HttpClient(CIO)
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun searchForReleaseGroup(albumTitle: String, artistName: String): ReleaseGroup {
        val response = client.get(RELEASE_GROUP_ENDPOINT) {
            accept(ContentType.Application.Json)
            url {
                parameters.append(
                    "query",
                    "\"$albumTitle\" AND artist:\"$artistName\""
                )
            }
        }
        val releaseGroup: ReleaseGroup = run {
            val payload: JsonObject = json.decodeFromString(response.body())
            val releaseGroups: JsonArray = payload["release-groups"]!!.jsonArray
            json.decodeFromJsonElement(releaseGroups[0])
        }
        return releaseGroup
    }
}