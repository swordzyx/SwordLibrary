#include "android_trace.h"
#include <dlfcn.h>

/**
* 参考 https://developer.android.com/topic/performance/tracing/custom-events-native
*/
// 定义函数指针
void (*ATrace_beginSection_ptr)(const char*) = nullptr;
void (*ATrace_endSection_ptr)(void) = nullptr;

void init_android_trace_ptrs() {
    if (ATrace_beginSection_ptr == nullptr) {
        void* libandroid = dlopen("libandroid.so", RTLD_NOW | RTLD_LOCAL);
        if (libandroid) {
            ATrace_beginSection_ptr = (void (*)(const char*))dlsym(libandroid, "ATrace_beginSection");
            ATrace_endSection_ptr = (void (*)(void))dlsym(libandroid, "ATrace_endSection");
        }
    }
} 