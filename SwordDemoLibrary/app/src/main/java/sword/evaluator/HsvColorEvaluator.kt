package sword.evaluator

import android.animation.TypeEvaluator
import android.graphics.Color


/**
 * 用于实现颜色过渡的估值器
 *
 * 使用示例：
 * ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xff00ff00);
 * animator.setEvaluator(new HsvEvaluator());
 * animator.start();
 */
class HsvColorEvaluator: TypeEvaluator<Int> {
  private var startHsv: FloatArray = FloatArray(3)
  private var endHsv: FloatArray = FloatArray(3)
  private var outHsv: FloatArray = FloatArray(3)
  override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
    //1. 转成 HSV 颜色值
    Color.colorToHSV(startValue, startHsv)
    Color.colorToHSV(endValue, endHsv)

    //2. 计算 HSV 偏移的值
    if (endHsv[0] - startHsv[0] > 180) {
      endHsv[0] -= 360f
    } else if (endHsv[0] - startHsv[0] < -180) {
      endHsv[0] += 360f
    }
    outHsv[0] = startHsv[0] + (endHsv[0] - startHsv[0]) * fraction
    if (outHsv[0] > 360) {
      outHsv[0] -= 360f
    } else if (outHsv[0] < 0) {
      outHsv[0] += 360f
    }
    outHsv[1] = startHsv[1] + (endHsv[1] - startHsv[1]) * fraction
    outHsv[2] = startHsv[2] + (endHsv[2] - startHsv[2]) * fraction

    //3. 计算 alpha 通道值
    val alpha = startValue shr 24 + ((endValue shr 24 - startValue shr 24) * fraction).toInt()

    //4. 将 Hsv 转成 RGB 颜色值
    return Color.HSVToColor(alpha, outHsv)
  }
}