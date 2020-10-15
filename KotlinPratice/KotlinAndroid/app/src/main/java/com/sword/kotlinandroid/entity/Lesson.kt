package com.sword.kotlinandroid.entity

class Lesson {
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

    private var date: String? = null
    private var content: String? = null
    private var state: State? = null

    constructor(data: String?, content: String?, state: State?) {
        this.date = data
        this.content = content
        this.state = state
    }


}