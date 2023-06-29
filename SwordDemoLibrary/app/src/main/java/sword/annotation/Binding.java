package sword.annotation;

import com.sword.LogUtil;

import java.lang.reflect.Field;

public class Binding {
  private static final String tag = "Binding";
  public static void bind(AnnotationLinearLayout linearLayout) {
    //获取哪些字段使用的 @BindView 注解。每次执行 findViewById 都要反射，调用的次数很多时，会很耗性能
    for (Field field: linearLayout.getClass().getDeclaredFields()) {
      LogUtil.debug(tag, "字段名称：" + field.getName());
      BindView bindView = field.getAnnotation(BindView.class);
      if (bindView != null) {
        try {
          LogUtil.debug(tag, "view id: " + bindView.value());
          field.set(linearLayout, linearLayout.findViewById(bindView.value()));
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
