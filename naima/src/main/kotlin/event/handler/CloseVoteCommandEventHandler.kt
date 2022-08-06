package event.handler

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import system.SystemState

class CloseVoteCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    override fun handle() {
        event.deferReply().queue()
        val winner = SystemState.closeVotingRound()
        event.hook.sendMessage("Winner: ${winner.releaseGroup.prettyName}").queue()
    }
}
