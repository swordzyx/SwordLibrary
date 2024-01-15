package sword.utils
import sword.logger.SwordLog
import java.lang.Float

val FLOAT_EPSILON = Float.intBitsToFloat(1)

object Utils {
  fun print() {
    SwordLog.debug("Utils", "FLOAT_EPSILON: $FLOAT_EPSILON")
  }
}
