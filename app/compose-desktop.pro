# Kotlin serialization (kotlinx.serialization rules/common.pro)
-keepclassmembers @kotlinx.serialization.Serializable class ** {
    static ** Companion;
}
-if @kotlinx.serialization.internal.NamedCompanion class *
-keepclassmembers class * {
    static <1> *;
}
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-dontnote kotlinx.serialization.**
-dontwarn kotlinx.serialization.internal.ClassValueReferences
-keepclassmembers public class **$$serializer {
    private ** descriptor;
}

# Koin
-keep class org.koin.** { *; }
-keep interface org.koin.** { *; }

# SQLite JDBC (native driver)
-keep class org.sqlite.** { *; }

# Exposed (runtime schema / reflection paths)
-keep class org.jetbrains.exposed.** { *; }

# Commons Compress: app uses tar/gzip only; other codecs are optional transitive references
-dontwarn org.apache.commons.compress.harmony.**
-dontwarn org.apache.commons.compress.archivers.sevenz.**
-dontwarn org.apache.commons.compress.compressors.brotli.**
-dontwarn org.apache.commons.compress.compressors.lzma.**
-dontwarn org.apache.commons.compress.compressors.xz.**
-dontwarn org.apache.commons.compress.compressors.zstandard.**
-dontwarn org.objectweb.asm.**
-dontwarn org.tukaani.xz.**
-dontwarn org.brotli.dec.**
-dontwarn com.github.luben.zstd.**

# Application: Koin graph, Decompose, MVI, Compose generated resources; names may be obfuscated
-keep,allowobfuscation class ru.arheo.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

-dontwarn org.slf4j.**
