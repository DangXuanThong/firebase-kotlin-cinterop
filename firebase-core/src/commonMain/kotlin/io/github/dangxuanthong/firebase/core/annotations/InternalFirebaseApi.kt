package io.github.dangxuanthong.firebase.core.annotations

@RequiresOptIn(
    message = "Internal API of the Firebase SDK. Not for external use; may change without notice.",
    level = RequiresOptIn.Level.ERROR
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.TYPEALIAS
)
annotation class InternalFirebaseApi
