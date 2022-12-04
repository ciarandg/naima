package event.handler

import discord.embed.AlbumCoverEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import system.SystemState
import system.exception.VotingRoundNotYetOpenException

class CloseVoteCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    override fun handle() {
        event.deferReply().queue()
        try {
            val winner = SystemState.closeVotingRound()
            event.hook
                .sendMessage("Winner: ${winner.releaseGroup.prettyName}")
                .addEmbeds(AlbumCoverEmbed(winner.releaseGroup).build())
                .queue()
        } catch (e: VotingRoundNotYetOpenException) {
            event.hook.sendMessage("There is no voting round currently open")
        }
    }
}
