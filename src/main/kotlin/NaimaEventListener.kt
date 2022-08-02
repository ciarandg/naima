import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class NaimaEventListener : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Discord API is ready!")
        jda.updateCommands().addCommands(
            NaimaCommands.submitCommand
        ).complete()
        println("Updated commands!")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name == NaimaCommands.submitCommand.name) {
            event.deferReply().queue()
            val requester = event.user.name
            val artist = event.getOption("artist")!!.asString
            val album = event.getOption("album")!!.asString
            val coverEmbed = EmbedBuilder()
                .setImage("https://coverartarchive.org/release-group/77cf47ba-58cd-3f3d-a5f9-79bf89860421/front")
                .build()
            event.hook
                .sendMessage("$requester has requested `$album` by `$artist`")
                .addEmbeds(coverEmbed)
                .queue()
        }
    }
}