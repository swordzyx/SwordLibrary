package com.example.learnkotlin.lesson

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.R
import com.example.learnkotlin.core.BaseViewHolder

class LessonAdapter : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
    private var list: List<Lesson>  = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateAndNotify(list: List<Lesson>) {
        this.list = list
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder.onCreate(parent)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * kotlin 中没有显示修饰符的嵌套类和 Java 中的 static 嵌套类是一样的
     */
    class LessonViewHolder(item: View) : BaseViewHolder(item) {

        fun onBind(lesson: Lesson) {
            //设置内容
            var date = lesson.date
            if (date == null) {
                date = "日期待定"
            }
            setText(R.id.tv_date, date)
            setText(R.id.tv_content, lesson.content)
            setText(R.id.tv_state, lesson.state.stateName())

            //设置背景颜色
            val colorRes = when(lesson.state) {
                Lesson.State.PLAYBACK -> R.color.playback
                Lesson.State.LIVE -> R.color.live
                Lesson.State.WAIT -> R.color.wait
            }
            val backgroundColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemView.context.getColor(colorRes)
            } else {
                itemView.context.resources.getColor(colorRes)
            }
            getView<View>(R.id.tv_state).setBackgroundColor(backgroundColor)
        }

        companion object {
            fun onCreate(parent: ViewGroup): LessonViewHolder {
                return LessonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false))
            }
        }
    }
}