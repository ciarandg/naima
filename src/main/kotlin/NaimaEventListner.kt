import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class NaimaEventListner : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Discord API is ready!")
    }
}