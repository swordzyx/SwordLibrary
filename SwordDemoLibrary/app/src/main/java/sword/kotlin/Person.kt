package sword.kotlin

class Person {

    object InnerSingleton {
        @JvmStatic
        val instance by lazy {
            Person()
        }

        @JvmStatic
        var person: Person? = null

        @JvmStatic
        fun getInnerSingleton(): Person {
            return Person()
        }
    }
}