import Environment.botToken
import net.dv8tion.jda.api.JDABuilder

val jda = JDABuilder
    .createDefault(botToken)
    .build()

fun main(args: Array<String>) {
    println("Hello, fellow jazz heads!")
}