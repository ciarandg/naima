package system.data

import musicbrainz.data.ReleaseGroup
import java.time.Instant

data class Suggestion(
    val releaseGroup: ReleaseGroup,
    val suggesterUserId: String,
    val suggestedTimestamp: Instant,
    val timesVotable: Int = 0,
    val timesVoted: Int = 0,
    val hasBeenChosen: Boolean = false
)
