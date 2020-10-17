package com.sword.kotlinandroid.lesson

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sword.base.core.BaseViewHolder
import com.sword.kotlinandroid.R
import com.sword.kotlinandroid.entity.Lesson

class LessonAdapter: RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
    private var list: List<Lesson> = ArrayList()

    fun updateAndNotify(list: List<Lesson>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder.onCreate(parent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.onBind(list[position])
    }


    class LessonViewHolder: BaseViewHolder {
        constructor(itemView: View): super(itemView)

        companion object {
            fun onCreate(parent: ViewGroup): LessonViewHolder {
                return LessonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false))
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun onBind(lesson: Lesson) {
            var data: String? = lesson.date?:"日期待定"

            setText(R.id.tv_date, data)

            setText(R.id.tv_content, lesson.content)

            val state: Lesson.State? = lesson.state
            if (state != null) {
                setText(R.id.tv_state, state.stateName())
                var colorRes = when(state) {
                    Lesson.State.PLAYBACK -> R.color.playback
                    Lesson.State.LIVE -> R.color.live
                    Lesson.State.WAIT -> R.color.live
                }
                val backgroundColor: Int = itemView.context.getColor(colorRes)
                getView<TextView>(R.id.tv_state)?.setBackgroundColor(backgroundColor)
            }


        }
    }
}