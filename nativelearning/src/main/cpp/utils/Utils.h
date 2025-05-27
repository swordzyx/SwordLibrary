#ifndef CLASSIFICATION_UTILS_H
#define CLASSIFICATION_UTILS_H
#include <android/log.h>

extern bool isDebug;
#define LOG_TAG "Classification_native"
#define debug_log(...) \
    do { \
        if(isDebug) { \
            ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)); \
        } \
    } while(0)
#define info_log(...) ((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__))
#define error_log(...) ((void)__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__))

#define TEST_MODE
bool licenseValid();

#endif 
