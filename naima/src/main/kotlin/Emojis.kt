import com.vdurmont.emoji.EmojiManager
import net.dv8tion.jda.api.entities.emoji.Emoji

class Emojis {
    companion object {
        val voteEmojis = listOf(
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
        ).map { Emoji.fromUnicode(EmojiManager.getForAlias(it).unicode) }

        fun emojiToIndex(emoji: Emoji): Int {
            return voteEmojis.indexOf(emoji)
        }
    }
}