import event.handler.SubmitCommandEventHandler
import kotlinx.coroutines.runBlocking
import musicbrainz.MusicBrainz
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class NaimaEventListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Discord API is ready!")
        jda.updateCommands().addCommands(
            NaimaCommands.submitCommand,
            NaimaCommands.openVotingRoundCommand,
            NaimaCommands.closeVotingRoundCommand
        ).complete()
        println("Updated commands!")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            NaimaCommands.submitCommand.name -> SubmitCommandEventHandler(event).handle()
            else -> throw IllegalStateException("User a command that shouldn't exist: /${event.name}")
        }
    }
}
