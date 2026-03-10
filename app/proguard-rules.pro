# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line information for readable crash stacks in release (optional but useful)
-keepattributes SourceFile,LineNumberTable,Signature,*Annotation*

# ------------------------------------------------------------
# Retrofit / OkHttp / Gson keep rules for runtime reflection
# ------------------------------------------------------------

# Retrofit interfaces and method annotations
-keep interface retrofit2.** { *; }
-keep class retrofit2.** { *; }
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault

# Keep model classes used in API parsing (adjust package if needed)
-keep class dev.vili.spot.data.model.** { *; }

# Gson internals and TypeToken (generic collection parsing)
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keep class com.google.gson.stream.** { *; }

# Keep fields annotated for serialized names (if used now or later)
-keepclassmembers class ** {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Kotlin metadata used by reflection-based converters/adapters
-keep class kotlin.Metadata { *; }

# Keep OkHttp/Okio classes commonly touched by reflection/stack traces
-dontwarn okhttp3.**
-dontwarn okio.**

-keep interface dev.vili.spot.data.api.** { *; }
