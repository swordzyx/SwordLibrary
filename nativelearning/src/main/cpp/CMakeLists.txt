cmake_minimum_required(VERSION 3.22.1)

project("nativelearning")

include_directories(${CMAKE_CURRENT_LIST_DIR}/utils)

file(GLOB sources ${CMAKE_CURRENT_LIST_DIR}/*.cpp)
file(GLOB utils_src ${CMAKE_CURRENT_LIST_DIR}/utils/*.cpp)
add_library(${CMAKE_PROJECT_NAME} SHARED
        ${sources}
        ${utils_src})

target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log)