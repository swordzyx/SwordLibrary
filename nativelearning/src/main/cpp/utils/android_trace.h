#ifndef ANDROID_TRACE_UTILS_H
#define ANDROID_TRACE_UTILS_H

// 声明系统追踪函数指针，可在多个 cpp 文件中访问
extern void (*ATrace_beginSection_ptr)(const char*);
extern void (*ATrace_endSection_ptr)(void);

// 初始化函数指针，调用后可使用 ATrace_beginSection_ptr/ATrace_endSection_ptr
void init_android_trace_ptrs();

#endif // ANDROID_TRACE_UTILS_H 