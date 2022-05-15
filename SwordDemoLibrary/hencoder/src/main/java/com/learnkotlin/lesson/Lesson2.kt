package com.example.learnkotlin.lesson

/**
 * constructor(date: String?, content: String, state: State) 是主构造器
 * 主构造器可以放在类声明的后面，而原来在主构造器的初始化代码可以放到 init{...} 代码块中，在 init{..} 中可以直接访问到主构造器中的参数
 *
 * 在主构造器的参数前面加上 var 或者 val ，该参数会自动成为成员变量，默认为 public ，自动生成对应成员的 getter 和 setter 方法
 *
 * 在没有加 var 或者 val 自动就构造参数变为成员变量时，只有初始化代码块和成员变量的初始化可以访问到构造参数
 *
 * 因此类声明下面的 date ，content，state 三个成员属性的声明可以删掉，直接在构造参数前面加上 var
 */
class Lesson2 constructor(var date: String?, var content: String, var state: State) {

    /**
     * 将 private 删掉，成员默认为 public ，自动生成 getter 和 setter 方法。因此下面三行代码中的 private 其实都可以删掉
     */
    //private var date: String? = null
    //private var content: String? = null
    //private var state: State? = null

    init {
        //等号右边的 date，content，state 就是主构造器中的参数
        this.date = date
        this.content = content
        this.state = state
    }

    enum class State {
        PLAYBACK {
            override fun stateName(): String {
                return "有回放"
            }
        },

        LIVE {
            override fun stateName(): String {
                return "正在直播"
            }
        },

        WAIT {
            override fun stateName(): String {
                return "等待中"
            }
        };
        abstract fun stateName(): String
    }

    operator fun component1(): String? {
        return date
    }

    operator fun component2(): String {
        return content
    }

    operator fun component3(): State {
        return state
    }

    fun main() {
        val les = Lesson2("1.1", "kotlin", State.LIVE)

        val (date1: String?, content1: String, state1: State) = les
    }
}
