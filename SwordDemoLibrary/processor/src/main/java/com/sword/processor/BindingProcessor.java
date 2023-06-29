package com.sword.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sword.annotation.BindView;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;


public class BindingProcessor extends AbstractProcessor {
  private Filer filer;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    filer = processingEnv.getFiler();
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    //创建 XxxxActivityBinding 类，获取 BindView 注解所在的 Element，也就是使用了 BindView 注解的类
    for (Element element : roundEnvironment.getRootElements()) {
      //获取包名
      String packageStr = element.getEnclosingElement().toString();
      //获取使用了 BindView 注解的类的类名
      String classStr = element.getSimpleName().toString();
      //创建类名信息
      ClassName className = ClassName.get(packageStr, classStr + "Binding");
      //创建构造方法信息
      MethodSpec.Builder methodSpecBuilder = MethodSpec.constructorBuilder()
          .addModifiers(Modifier.PUBLIC)
          .addParameter(ClassName.get(packageStr, classStr), "activity");
      
      boolean hasBinding = false;

      for (Element enclosedElement : element.getEnclosedElements()) {
        if (enclosedElement.getKind() == ElementKind.FIELD) {
          BindView bindView = enclosedElement.getAnnotation(BindView.class);
          if (bindView != null) {
            methodSpecBuilder.addStatement("activity.$N = activity.findViewById($L)", 
                enclosedElement.getSimpleName(), bindView.value());
            hasBinding = true;
          }
        }
      }

      //组装完整的类的内容
      TypeSpec classSpec = TypeSpec.classBuilder(className)
          .addModifiers(Modifier.PUBLIC)
          .addMethod(methodSpecBuilder.build())
          .build();
      if (hasBinding) {
        try {
          JavaFile.builder(packageStr, classSpec).build().writeTo(filer);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(BindView.class.getCanonicalName());
  }
}
