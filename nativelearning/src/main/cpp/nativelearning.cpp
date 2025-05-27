#include <jni.h>
#include <string>
#include "android_trace.h"

extern "C" JNIEXPORT jstring JNICALL
stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved __unused) {
    JNIEnv *env;
    if(vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    jclass c = env->FindClass("com/xsyt/nativelearning/NativeLib");
    if (c == nullptr) return JNI_ERR;
    
    static const JNINativeMethod methods[] = {
            {"stringFromJNI", "()Ljava/lang/String;", (void*)stringFromJNI}
    };
    int rc = env->RegisterNatives(c, methods, sizeof(methods) / sizeof(methods[0]));
    if (rc < 0) return JNI_ERR;
    
    return JNI_VERSION_1_6;
}