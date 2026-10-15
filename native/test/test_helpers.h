#ifndef TEST_HELPERS_H
#define TEST_HELPERS_H

#include <iostream>
#include <cstdlib>

extern int failed_count;

#define ASSERT_TRUE(condition, message) \
    if (condition) { \
        std::cout << "  PASS: " << (message) << "\n"; \
    } else { \
        std::cout << "  FAIL: " << (message) << " (at " << __FILE__ << ":" << __LINE__ << ")\n"; \
        failed_count++; \
    }

#define ASSERT_NOT_NULL(ptr, message) ASSERT_TRUE((ptr) != nullptr, message)
#define ASSERT_NULL(ptr, message) ASSERT_TRUE((ptr) == nullptr, message)

#endif // TEST_HELPERS_H
