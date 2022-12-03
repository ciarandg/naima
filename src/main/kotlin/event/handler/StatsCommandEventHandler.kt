package event.handler

import database
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class StatsCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    override fun handle() {
        event.hook.sendMessage("""
            Total suggestions: ${database.suggestions.countDocuments()}
            Total voting rounds: ${database.votingRounds.countDocuments()}
        """.trimIndent())
    }
}