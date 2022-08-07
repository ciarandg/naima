package event.handler

import com.vdurmont.emoji.EmojiManager
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import system.SystemState
import system.data.Suggestion

class OpenVoteCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    private fun formatSelection(selection: List<Suggestion>) =
        if (selection.isEmpty()) {
            "No albums available for vote"
        } else {
            selection.mapIndexed { i, pick ->
                "${i + 1}. ${pick.releaseGroup.prettyName}"
            }.reduce { acc, s ->
                acc + "\n" + s
            }
        }

    override fun handle() {
        event.deferReply().queue()
        val round = SystemState.openVotingRound()
        val message = event.hook.sendMessage(formatSelection(round.choices)).complete()
        val emojis = Emojis.voteEmojis.take(round.choices.size)
        emojis.forEach { message.addReaction(it).queue() }
        SystemState.votingMessage = message
    }
}
