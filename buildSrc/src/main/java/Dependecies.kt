import Versions.androidCoreVersion
import Versions.androidJUnitVersin
import Versions.appCompatVersion
import Versions.assertJVersion
import Versions.constraintLayoutVersion
import Versions.daggerVersion
import Versions.espressoVersion
import Versions.glideVersion
import Versions.hdodenhofVersion
import Versions.jUnitVersion
import Versions.javaxAnnotationVersion
import Versions.javaxInjectVersion
import Versions.kotlinExtensionVersion
import Versions.kotlinVersion
import Versions.ktxVersion
import Versions.legacySupportVersion
import Versions.lifecycleVersion
import Versions.lottieVersion
import Versions.materialDesignVersion
import Versions.mockitoKotlinVersion
import Versions.recyclerViewVersion
import Versions.roboelectricVersion
import Versions.roomVersion
import Versions.rxKotlinVersion
import Versions.rxandroidVersion
import Versions.timberVersion
import Versions.truthVersion

object Config {
    val compileSdkVersion = 29
    val minSdkVersion = 19
    val targetSdkVersion = 29
    val versionCode = 1
    val versionName = "1.0.0"
}

object Versions {
    val kotlinVersion = "1.3.61"
    val javaxAnnotationVersion = "1.0"
    val javaxInjectVersion = "1"
    val rxKotlinVersion = "2.4.0"
    val jUnitVersion = "4.12"
    val mockitoKotlinVersion = "2.2.0"
    val assertJVersion = "3.8.0"
    val lifecycleVersion = "2.1.0"
    val roomVersion = "2.2.5"
    val roboelectricVersion = "4.3"
    val daggerVersion = "2.16"
    val glideVersion = "4.6.1"
    val timberVersion = "4.7.0"
    val rxandroidVersion = "2.1.1"
    val recyclerViewVersion = "1.0.0"
    val appCompatVersion = "1.1.0"
    val kotlinExtensionVersion = "1.1.0"
    val constraintLayoutVersion = "2.0.0-beta4"
    val hdodenhofVersion = "3.0.1"
    val legacySupportVersion = "1.0.0"
    val materialDesignVersion = "1.1.0-alpha10"
    val ktxVersion = "1.1.0"
    val lottieVersion = "3.3.1"


    //testing
    val androidCoreVersion = "1.2.0"
    val androidJUnitVersin = "1.1.1"
    val truthVersion = "0.42"
    val espressoVersion = "3.2.0"
}

object Libraries {
    //android
    val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewVersion"
    val appCompat = "androidx.appcompat:appcompat:$appCompatVersion"
    val kotlinExtensions = "androidx.core:core-ktx:$kotlinExtensionVersion"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
    val hdodenhof = "de.hdodenhof:circleimageview:$hdodenhofVersion"
    val androiXLegacy = "androidx.legacy:legacy-support-v4:$legacySupportVersion"
    val materialDesign = "com.google.android.material:material:$materialDesignVersion"
    val androidXExtensions = "androidx.core:core-ktx:$ktxVersion"
    val glide = "com.github.bumptech.glide:glide:$glideVersion"
    val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"

    //annotation
    val javaxAnnotation = "javax.annotation:jsr250-api:$javaxAnnotationVersion"
    val javaxInject = "javax.inject:javax.inject:$javaxInjectVersion"

    //rxJava
    val rxandroid = "io.reactivex.rxjava2:rxandroid:$rxandroidVersion"
    val rxKotlin = "io.reactivex.rxjava2:rxkotlin:$rxKotlinVersion"
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"

    //room
    val roomRunTime = "androidx.room:room-runtime:$roomVersion"
    val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    val roomExtensionAndCoroutine = "androidx.room:room-ktx:$roomVersion"
    val roomRxJava = "androidx.room:room-rxjava2:$roomVersion"
    // optional - Guava support for Room, including Optional and ListenableFuture
    val roomGuava = "androidx.room:room-guava:$roomVersion"

    //lifecycle libraries
    val lifecyleRuntime = "androidx.lifecycle:lifecycle-runtime:$lifecycleVersion"
    val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
    val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion"
    val lifecycleRxStreams = "androidx.lifecycle:lifecycle-reactivestreams:$lifecycleVersion"

    //dagger
    val dagger = "com.google.dagger:dagger-android:$daggerVersion"
    val daggerSupport = "com.google.dagger:dagger-android-support:$daggerVersion"
    val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    val daggerProcessor = "com.google.dagger:dagger-android-processor:$daggerVersion"

    //logging
    val timber = "com.jakewharton.timber:timber:$timberVersion"

    //animations
    val lottie = "com.airbnb.android:lottie:$lottieVersion"
}

object TestLibraries {
    //Testing dependencies
    val jUnit = "junit:junit:$jUnitVersion"
    val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVersion"
    val assertJ = "org.assertj:assertj-core:$assertJVersion"
    val roboelectric = "org.robolectric:robolectric:$roboelectricVersion"
    val lifecycleTest = "androidx.arch.core:core-testing:$lifecycleVersion"
    val roomTest = "androidx.room:room-testing:$roomVersion"


    val androidXJUnit = "androidx.test.ext:junit:$androidJUnitVersin"
    // Core library
    val androidXCore = "androidx.test:core:$androidCoreVersion"

    // AndroidJUnitRunner and JUnit Rules
    val androidXTestRunnner = "androidx.test:runner:$androidCoreVersion"
    val androidXTestRules = "androidx.test:rules:$androidCoreVersion"

    // Assertions
    val androidXTruth = "androidx.test.ext:truth:$androidCoreVersion"
    val googleTruth = "com.google.truth:truth:$truthVersion"

    // Espresso dependencies
    val espressoCore = "androidx.test.espresso:espresso-core:$espressoVersion"
    val espressoContrib = "androidx.test.espresso:espresso-contrib:$espressoVersion"
    val espresooIntent = "androidx.test.espresso:espresso-intents:$espressoVersion"
    val espressoAccessiblity = "androidx.test.espresso:espresso-accessibility:$espressoVersion"
    val espressoWeb = "androidx.test.espresso:espresso-web:$espressoVersion"
    val espresspIdlineConcurrent = "androidx.test.espresso.idling:idling-concurrent:$espressoVersion"
    val espresso = "androidx.test.espresso:espresso-idling-resource:$espressoVersion"
}