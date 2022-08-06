package event.handler

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

abstract class EventHandler(protected val event: SlashCommandInteractionEvent) {
    abstract fun handle()
}