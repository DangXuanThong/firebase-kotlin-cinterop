#!/usr/bin/env bash
set -euo pipefail

# 1. Declare constants
SDK_VER="v14.0.0"
WORK_DIR="./temp_sdk_dir"
INSTALL_DIR="./native/firebase-cpp-sdk"

# Store absolute paths so directory navigation does not break relative paths
ORIGINAL_DIR="$(pwd)"
ABS_WORK_DIR="$(cd "$(dirname "$0")/.." && pwd)/${WORK_DIR#./}"
ABS_INSTALL_DIR="$(cd "$(dirname "$0")/.." && pwd)/${INSTALL_DIR#./}"

# 2. Clone the repo tag $SDK_VER from https://github.com/firebase/firebase-cpp-sdk into $WORK_DIR
echo "==> Step 2: Cloning firebase-cpp-sdk tag $SDK_VER into $WORK_DIR..."
rm -rf "$ABS_WORK_DIR"
git clone --depth 1 --branch "$SDK_VER" https://github.com/firebase/firebase-cpp-sdk.git "$ABS_WORK_DIR"
cd "$ABS_WORK_DIR"

# 3. Create build directory and configure CMake
echo "==> Step 3: Configuring CMake..."
mkdir -p build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Release \
     -DFIREBASE_INCLUDE_LIBRARY_DEFAULT=OFF \
     -DFIREBASE_INCLUDE_AUTH=ON \
     -DFIREBASE_INCLUDE_FIRESTORE=ON \
     -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
     -DFIREBASE_LINUX_USE_CXX11_ABI=ON

# 4. Build target firebase_firestore in parallel
echo "==> Step 4: Compiling Firebase targets..."
cmake --build . --target firebase_firestore --parallel "$(nproc)" -- -Wno-author

# 5. Return to SDK work directory and create install directory structure
echo "==> Step 5: Preparing install directory at $INSTALL_DIR..."
cd "$ABS_WORK_DIR"
mkdir -p "$ABS_INSTALL_DIR/lib" "$ABS_INSTALL_DIR/include"

# 6. Copy all static library (.a) files to $INSTALL_DIR/lib
echo "==> Step 6: Copying compiled static libraries (.a)..."
find build/ -name "*.a" -exec cp {} "$ABS_INSTALL_DIR/lib/" \;

# 7. Copy App C++ include headers
echo "==> Step 7: Copying App headers..."
if [ -d "app/src/include/firebase" ]; then
  cp -r app/src/include/firebase "$ABS_INSTALL_DIR/include/"
fi

# 8. Copy Auth C++ include headers
echo "==> Step 8: Copying Auth headers..."
if [ -d "auth/src/include/firebase" ]; then
  cp -r auth/src/include/firebase/* "$ABS_INSTALL_DIR/include/firebase/"
fi

# 9. Copy Firestore C++ include headers
echo "==> Step 9: Copying Firestore headers..."
if [ -d "firestore/src/include/firebase" ]; then
  cp -r firestore/src/include/firebase/* "$ABS_INSTALL_DIR/include/firebase/"
fi
if [ -d "build/external/src/firestore/Firestore/core/include/firebase" ]; then
  cp -r build/external/src/firestore/Firestore/core/include/firebase/* "$ABS_INSTALL_DIR/include/firebase/"
fi

# 10. Remove $WORK_DIR directory
echo "==> Step 10: Cleaning up temporary work directory..."
cd "$ORIGINAL_DIR"
rm -rf "$ABS_WORK_DIR"

echo "==> Done! Firebase C++ SDK installed to $INSTALL_DIR"
