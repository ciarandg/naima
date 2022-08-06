package event.handler

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CloseVoteCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    override fun handle() {
        event.deferReply().queue()
    }
}