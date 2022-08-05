/**
 * Wrapper around whatever dbms we use (currently MongoDB)
 */
class Database {
    /**
     * Singleton pattern
     */
    companion object {
        var instance: Database? = null

        fun get(): Database {
            if (instance == null) {
                instance = Database()
            }
            return instance!!
        }
    }

    init {
    }
}
