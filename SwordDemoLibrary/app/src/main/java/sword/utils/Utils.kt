package sword.utils
import sword.logger.SwordLog
import java.text.DecimalFormat

val FLOAT_EPSILON = java.lang.Float.intBitsToFloat(1)

object Utils {
  fun print() {
    SwordLog.debug("Utils", "FLOAT_EPSILON: $FLOAT_EPSILON")
  }
}

private val formatter = DecimalFormat("###,###,##0.0")
fun kotlin.Float.toPercent(): String {
  return "${formatter.format(this)}%"
}
