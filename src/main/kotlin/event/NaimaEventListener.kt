package event

import discord.NaimaCommands
import event.handler.CloseVoteCommandEventHandler
import event.handler.OpenVoteCommandEventHandler
import event.handler.StatsCommandEventHandler
import event.handler.SuggestCommandEventHandler
import jda
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class NaimaEventListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Discord API is ready!")
        jda.updateCommands().addCommands(
            NaimaCommands.suggestCommand,
            NaimaCommands.openVoteCommand,
            NaimaCommands.closeVoteCommand,
            NaimaCommands.statsCommand
        ).complete()
        println("Updated commands!")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            NaimaCommands.suggestCommand.name -> SuggestCommandEventHandler(event).handle()
            NaimaCommands.openVoteCommand.name -> OpenVoteCommandEventHandler(event).handle()
            NaimaCommands.closeVoteCommand.name -> CloseVoteCommandEventHandler(event).handle()
            NaimaCommands.statsCommand.name -> StatsCommandEventHandler(event).handle()
            else -> throw IllegalStateException("User a command that shouldn't exist: /${event.name}")
        }
    }
}
