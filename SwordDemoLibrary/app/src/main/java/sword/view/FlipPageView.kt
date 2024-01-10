package sword.view

import android.animation.*
import android.content.Context
import android.content.res.Resources
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import com.example.swordlibrary.R
import sword.BitmapUtil
import sword.logger.SwordLog
import sword.dp2px

/**
 * 翻页效果 View
 */
class FlipPageView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val tag = "FlipPageView"
    private val angle = 30f
    private val imageWidth = dp2px(200f)
    private val imageBitmap =
        BitmapUtil.createBitmap(resources, R.drawable.avatar_rengwuxian, imageWidth, imageWidth)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val camera = Camera().apply {
        setLocation(0f, 0f, locationZ * Resources.getSystem().displayMetrics.density)
    }

    /**
     * 底部旋转的角度
     */
    var bottomFlip = 0f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 顶部翻转的角度
     */
    private var topFlip = 0f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 切线的角度
     */
    private var tangentAngle = 0f
        set(value) {
            field = value
            invalidate()
        }

    //动画相关
    private var animatorSet: AnimatorSet? = null

    private var valueHolderAnimator: ValueAnimator? = null

    init {
        setPadding(100, 100, 0, 0)
        initAnimationByAnimatorSet()
        //initAnimationByPropertyValuesHolder()
    }

    /**
     * 使用 AnimatorSet 依次对 FlipPageView 的 bottomFlip、tangentAngle、topFlip 这三个属性做动画
     */
    private fun initAnimationByAnimatorSet() {
        //动画相关
        animatorSet = AnimatorSet().apply {
            addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator) {
                    SwordLog.debug(tag, "onAnimationStart")
                }

                override fun onAnimationEnd(animation: Animator) {
                    SwordLog.debug(tag, "onAnimationEnd")
                    start()
                }

                override fun onAnimationCancel(animation: Animator) {
                    SwordLog.debug(tag, "onAnimationCancel")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    SwordLog.debug(tag, "onAnimationRepeat")
                }

            })
        }

        val bottomFlipAnimator =
            ObjectAnimator.ofFloat(this, "bottomFlip", 0f, 60f).apply {
                duration = 2000
                startDelay = 1000
            }
        val topFlipAnimator = ObjectAnimator.ofFloat(this, "topFlip", 0f, -60f).apply {
            startDelay = 200
            duration = 1000
        }
        val tangentAngleAniamtor = ObjectAnimator.ofFloat(this, "tangentAngle", 0f, 180f).apply {
            startDelay = 200
            duration = 1000
        }
        animatorSet!!.playSequentially(bottomFlipAnimator, tangentAngleAniamtor, topFlipAnimator)
    }

    /**
     * 使用 PropertyValuesHolder 同时对 FlipPageView 的 bottomFlip、tangentAngle、topFlip 这三个属性做动画
     */
    private fun initAnimationByPropertyValuesHolder() {
        val bottomFlipValueHolder = PropertyValuesHolder.ofFloat("bottomFlip", 0f, 60f)
        val tangentAngleValueHolder = PropertyValuesHolder.ofFloat("tangentAngle", 0f, 180f)
        val topFlipValueHolder = PropertyValuesHolder.ofFloat("topFlip", 0f, -60f)
        valueHolderAnimator = ObjectAnimator.ofPropertyValuesHolder(this,
            bottomFlipValueHolder,
            tangentAngleValueHolder,
            topFlipValueHolder
        ).apply {
            startDelay = 1000
            duration = 2000
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> (paddingLeft + imageWidth + paddingRight).toInt()
            else -> MeasureSpec.getSize(widthMeasureSpec)
        }
        val measureHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> (paddingTop + imageWidth + paddingBottom).toInt()
            else -> MeasureSpec.getSize(heightMeasureSpec)
        }
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        //绘制上半部分
        canvas.withSave {
            canvas.translate(paddingLeft + imageWidth / 2, paddingTop + imageWidth / 2)
            canvas.rotate(-tangentAngle)
            camera.save()
            camera.rotateX(topFlip)
            camera.applyToCanvas(this)
            camera.restore()
            canvas.clipRect(
                -imageWidth, -imageWidth, imageWidth, 0f
            )
            canvas.rotate(tangentAngle)
            canvas.translate(-(paddingLeft + imageWidth / 2), -(paddingTop + imageWidth / 2))
            canvas.drawBitmap(
                imageBitmap, paddingLeft.toFloat(), paddingTop.toFloat(), paint
            )
        }

        //绘制下半部分
        canvas.withSave {
            canvas.translate(paddingLeft + imageWidth / 2f, paddingTop + imageWidth / 2f)
            canvas.rotate(-tangentAngle)
            camera.save()
            camera.rotateX(bottomFlip)
            camera.applyToCanvas(this)
            camera.restore()
            canvas.clipRect(
                -imageWidth, 0f, imageWidth, imageWidth
            )
            canvas.rotate(tangentAngle)
            canvas.translate(-(paddingLeft + imageWidth / 2f), -(paddingTop + imageWidth / 2f))
            canvas.drawBitmap(
                imageBitmap, paddingLeft.toFloat(), paddingTop.toFloat(), paint
            )
        }
    }

    fun startSequentiallyAnimate() {
        if (animatorSet != null) {
            animatorSet!!.start()
        } else if (valueHolderAnimator != null) {
            valueHolderAnimator!!.start()
        }
    }
}