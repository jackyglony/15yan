-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepattributes *Annotation*, Signature
# For Guava:
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

# For RxJava:
-dontwarn rx.**

# nio
-dontwarn java.nio.file.*

# support-v4
-keep class android.support.v4.** { *; }

# support-v8
-keep class android.support.v8.renderscript.** { *; }

# jar
#-libraryjars ./libs/jsoup-1.8.1.jar
-keep class org.jsoup.** {*;}

#-libraryjars ./libs/nineoldandroids-2.4.0.jar
-keep class com.nineoldandroids.** {*;}

# Gson
-keep public class com.google.gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class sun.misc.Unsafe { *; }
-keepattributes Expose
-keepattributes SerializedName
-keepattributes Since
-keepattributes Until
-keepattributes Signature
-keepclasseswithmembers class * { @com.google.gson.annotations.Expose <fields>; }

# Dagger
-dontwarn dagger.internal.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}
-keep class dagger.** { *; }
-keep class dagger.internal.** { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection
-keep class * extends dagger.internal.BindingsGroup

#Keep the dagger annotation classes themselves
-keep @interface dagger.*,javax.inject.*

#Keep the Modules intact
-keep @dagger.Module class *

#-Keep the names of classes that have fields annotated with @Inject and the fields themselves.
-keepclasseswithmembernames class * {
  @javax.inject.* <fields>;
}

# Keep the generated classes by dagger-compile
-keep class **$$ModuleAdapter
-keep class **$$InjectAdapter
-keep class **$$StaticInjection

-keepnames class org.liuyichen.fifteenyan.fragment.*

#okio
-dontwarn okio.**

#okhttp
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# Retrofit
-keep class retrofit.** { *; }
-dontwarn retrofit.appengine.*
-keepclasseswithmembers class * {
    @retrofit.** *;
}

# Picasso
-keep class com.squareup.picasso.** { *; }
-keepclasseswithmembers class * {
    @com.squareup.picasso.** *;
}

#ollie
-keep class ollie.** { *; }
-keep class * extends ollie.internal.ModuleAdapter
-keep class * extends ollie.OllieProvider
-keep class * extends ollie.TypeAdapter
-keep class * extends ollie.Model
-keep class * extends ollie.Migration

#APP
-keep class org.liuyichen.fifteenyan.module.** { *; }

# disable log
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);

    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class org.liuyichen.fifteenyan.R$*{
    public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}