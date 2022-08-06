import Environment.botToken
import event.NaimaEventListener
import net.dv8tion.jda.api.JDABuilder

val database = Database()

val jda = JDABuilder
    .createDefault(botToken)
    .addEventListeners(NaimaEventListener())
    .build()

fun main(args: Array<String>) = Unit
