package event.handler

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class StatsCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    override fun handle() {
        event.hook.sendMessage("""
            Print statistics here
        """.trimIndent())
    }
}