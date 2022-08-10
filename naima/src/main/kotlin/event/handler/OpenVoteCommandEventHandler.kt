package event.handler

import discord.Emojis
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import system.SystemState
import system.exception.NoAlbumsAvailableException
import system.exception.VotingRoundAlreadyOpenException

class OpenVoteCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    override fun handle() {
        event.deferReply().queue()
        try {
            val round = SystemState.openVotingRound(event.hook)
            val emojis = Emojis.voteEmojis.take(round.round.choices.size)
            emojis.forEach { round.fetchVotingMessage().addReaction(it).queue() }
        } catch (e: VotingRoundAlreadyOpenException) {
            event.hook.sendMessage(
                "You need to close the currently open voting round with `/close` before opening a new one"
            ).queue()
        } catch (e: NoAlbumsAvailableException) {
            event.hook.sendMessage(
                "Couldn't open a new voting round because there are no albums available to vote on"
            ).queue()
        }
    }
}
