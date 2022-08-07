package event.handler

import com.vdurmont.emoji.EmojiManager
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import system.SystemState
import system.data.Suggestion

class OpenVoteCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    override fun handle() {
        event.deferReply().queue()
        val round = SystemState.openVotingRound(event.hook)
        val emojis = Emojis.voteEmojis.take(round.choices.size)
        emojis.forEach { round.fetchVotingMessage().addReaction(it).queue() }
    }
}
