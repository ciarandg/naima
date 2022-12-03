package discord

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

object NaimaCommands {
    val suggestCommand = Commands.slash("suggest", "Suggest an album")
        .addOption(OptionType.STRING, "artist", "The album artist", true)
        .addOption(OptionType.STRING, "album", "The album's title", true)
    val openVoteCommand = Commands.slash("open", "Open a voting round")
    val closeVoteCommand = Commands.slash("close", "Close the currently open voting round")
    val statsCommand = Commands.slash("stats", "Get some statistics about the album catalog")
}
