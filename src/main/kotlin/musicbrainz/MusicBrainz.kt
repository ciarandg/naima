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
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

object MusicBrainz {
    private const val RELEASE_GROUP_ENDPOINT = "https://musicbrainz.org/ws/2/release-group"
    private val client = HttpClient(CIO)

    suspend fun searchForReleaseGroup(albumTitle: String, artistName: String): JsonObject {
        val response = client.get(RELEASE_GROUP_ENDPOINT) {
            accept(ContentType.Application.Json)
            url {
                parameters.append(
                    "query",
                    "\"$albumTitle\" AND artist:\"$artistName\""
                )
            }
        }
        val releaseGroup = run {
            val payload: JsonObject = Json.decodeFromString(response.body())
            val releaseGroups: JsonArray = payload["release-groups"]!!.jsonArray
            releaseGroups[0].jsonObject
        }
        return releaseGroup
    }
}