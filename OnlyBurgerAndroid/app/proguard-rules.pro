# Default ProGuard rules. R8 is not enabled for debug builds.
# Retrofit/Gson model classes are accessed reflectively; keep the DTOs.
-keep class com.onlyburger.app.data.remote.dto.** { *; }
