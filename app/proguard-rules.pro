# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/chayansoni/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and change the file name to refer to other
# files.

-keep class com.example.hindilearn.data.** { *; }
-keep class com.example.hindilearn.srs.** { *; }
-keepattributes Signature, *Annotation*, InnerClasses, EnclosingMethod
