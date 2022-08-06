package event.handler

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.lang.Integer.min

class OpenVoteCommandEventHandler(event: SlashCommandInteractionEvent) : EventHandler(event) {
    private fun pullSelection(): Set<String> {
        val selection: MutableSet<String> = mutableSetOf()
        val suggestionsAvailable = unsuggested.size + suggested.size
        while (selection.size < min(ALBUMS_PER_ROUND, suggestionsAvailable)) {
            val pickFrom = if (unsuggested.isEmpty()) suggested else unsuggested
            val pick = pickFrom.random()
            pickFrom.remove(pick)
            selection.add(pick)
        }
        return selection
    }

    override fun handle() {
        event.deferReply().queue()
        val selection = pullSelection()
        event.hook.sendMessage(selection.toString()).queue()
    }

    companion object {
        private const val ALBUMS_PER_ROUND = 3
        private val unsuggested = mutableSetOf(
            "The Clash - London Calling",
            "Kanye West - Yeezus",
            "Novos Baianos - Acabou Chorare",
            "Boris - Akuma no Uta",
            "Stereolab - Dots and Loops",
            "Bob Dylan - Blonde on Blonde"
        )

        private val suggested = mutableSetOf(
            "Cannibal Ox - The Cold Vein",
            "Nick Drake - Bryter Layter",
            "Fennesz - Endless Summer",
            "Autechre - Chiastic Slide"
        )
    }
}
