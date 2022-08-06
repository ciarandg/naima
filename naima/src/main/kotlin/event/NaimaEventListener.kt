package event

import command.NaimaCommands
import Database
import event.handler.CloseVoteCommandEventHandler
import event.handler.OpenVoteCommandEventHandler
import event.handler.SubmitCommandEventHandler
import jda
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class NaimaEventListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Discord API is ready!")
        jda.updateCommands().addCommands(
            NaimaCommands.submitCommand,
            NaimaCommands.openVoteCommand,
            NaimaCommands.closeVoteCommand
        ).complete()
        println("Updated commands!")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            NaimaCommands.submitCommand.name -> SubmitCommandEventHandler(event).handle()
            NaimaCommands.openVoteCommand.name -> OpenVoteCommandEventHandler(event).handle()
            NaimaCommands.closeVoteCommand.name -> CloseVoteCommandEventHandler(event).handle()
            else -> throw IllegalStateException("User a command that shouldn't exist: /${event.name}")
        }
    }
}
