package musicbrainz

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType

object MusicBrainz {
    private const val RELEASE_GROUP_ENDPOINT = "https://musicbrainz.org/ws/2/release-group"
    private val client = HttpClient(CIO)

    suspend fun searchForReleaseGroup(albumTitle: String, artistName: String): String {
        val response = client.get(RELEASE_GROUP_ENDPOINT) {
            accept(ContentType.Application.Json)
            url {
                parameters.append(
                    "query",
                    "\"$albumTitle\" AND artist:\"$artistName\""
                )
            }
        }
        return response.body()
    }
}