import musicbrainz.data.ReleaseGroup

class SuggestionPool {
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

    fun submit(album: ReleaseGroup) { }

    fun poll(count: Int): Set<String> {
        val selection: MutableSet<String> = mutableSetOf()
        val suggestionsAvailable = unsuggested.size + suggested.size
        while (selection.size < Integer.min(count, suggestionsAvailable)) {
            val pickFrom = if (unsuggested.isEmpty()) suggested else unsuggested
            val pick = pickFrom.random()
            pickFrom.remove(pick)
            selection.add(pick)
        }
        return selection
    }
}