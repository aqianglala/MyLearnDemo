# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\DevelopeSoft\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


#-dontusemixedcaseclassnames#不使用大小写形式的混淆名
#-dontskipnonpubliclibraryclasses#不跳过library的非public的类
#-dontoptimize#不进行优化，优化可能会在某些手机上无法运行。
#-dontpreverify#不净行预校验，该校验是java平台上的，对android没啥用处
#-keepattributes *Annotation*#对注解中的参数进行保留
#-keep public class com.deep.test.MainActivity //对某个class不进行混淆
#-dontshrink #不缩减代码，需要注意，反射调用的代码会被认为是无用代码而删掉，所以要提前keep出来
#-keepclassmembers enum * {
#public static **[] values();
#public static ** valueOf(java.lang.String);
#}#保持枚举类中的属性不被混淆
#-keepclassmemberspublic class xxx extends xxx{
#void set*(***);
#*** get*();
#}#不混淆任何view子类的get和set方法。
#-keepclassmembers class * implements android.os.Parcelable {
#public static final android.os.Parcelable$Creator CREATOR;
#}#aidl文件不能去混淆
#-keep public class com.ebt.app.common.bean.Customer
##保留某个类名不被混淆
#-keep public class com.ebt.app.common.bean.Customer { *;}
##保留类及其所有成员不被混淆
#-keep public class com.ebt.app.common.bean.Customer {
#static final;
#private void get*();
#}#只保留类名及其部分成员不被混淆
#-keep class com.ebt.app.sync.** { *;}
##保留包路径下所有的类及其属性和方法
#-keepclassmembers class **.R$* {
#public static ;
#}#资源类变量需要保留
