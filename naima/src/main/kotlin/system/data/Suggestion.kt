package system.data

import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.entities.User
import java.time.Instant
import java.time.LocalDateTime

data class Suggestion(
    val releaseGroup: ReleaseGroup,
    val suggester: User,
    val suggestedTimestamp: Instant,
    val timesVotable: Int = 0,
    val timesVoted: Int = 0
)
