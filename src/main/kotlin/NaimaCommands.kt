import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

object NaimaCommands {
    val submitCommand = Commands.slash("submit", "Submit an album")
        .addOption(OptionType.STRING, "artist", "The album artist", true)
        .addOption(OptionType.STRING, "album", "The album's title", true)
}
