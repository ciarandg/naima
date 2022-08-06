package event.handler

import com.vdurmont.emoji.EmojiManager
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import system.SuggestionPool
import system.VotingRound

class OpenVoteCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    private fun formatSelection(selection: Set<String>) =
        if (selection.isEmpty()) {
            "No albums available for vote"
        } else {
            selection.mapIndexed { i, pick ->
                "${i + 1}. $pick"
            }.reduce { acc, s ->
                acc + "\n" + s
            }
        }

    override fun handle() {
        event.deferReply().queue()
        val round = VotingRound(suggestionPool)
        val message = event.hook.sendMessage(formatSelection(round.choices)).complete()
        val emojis = voteEmojis.take(round.choices.size)
        emojis.forEach { message.addReaction(it).queue() }
    }

    companion object {
        private val suggestionPool = SuggestionPool()
        private val voteEmojis = listOf(
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
        ).map { Emoji.fromUnicode(EmojiManager.getForAlias(it).unicode) }
    }
}
