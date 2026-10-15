cmake_minimum_required(VERSION 3.15)
project(firebase_native CXX C)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(FIREBASE_SDK_DIR "${CMAKE_CURRENT_SOURCE_DIR}/firebase-cpp-sdk")

include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}/include
    ${FIREBASE_SDK_DIR}/include
)

# Collect all pre-compiled Firebase C++ static libraries
file(GLOB FIREBASE_LIBS "${FIREBASE_SDK_DIR}/lib/*.a")

# Find required Linux system dependencies
find_package(OpenSSL REQUIRED)
find_package(PkgConfig REQUIRED)
pkg_check_modules(LIBSECRET REQUIRED IMPORTED_TARGET libsecret-1)

# Build shared library libfirebase_native.so
add_library(firebase_native SHARED
    src/firebase_impl.cpp
)

target_link_libraries(firebase_native PRIVATE
    -Wl,--start-group
    ${FIREBASE_LIBS}
    # Don't re-export symbols from the static SDK archives
    -Wl,--exclude-libs,ALL
    OpenSSL::SSL
    OpenSSL::Crypto
    PkgConfig::LIBSECRET
    pthread
    dl
)

# Build C++ test executable
add_executable(test_main test/main.cpp test/test_app_options.cpp)
target_link_libraries(test_main PRIVATE firebase_native)
