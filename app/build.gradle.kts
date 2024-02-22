plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "edu.miguelangelmoreno.shoppinglistapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.miguelangelmoreno.shoppinglistapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding{
        enable = true
    }

}
kapt {
    correctErrorTypes =  true
}

dependencies {

    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Firebase
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.2")
    
    val navVersion = "2.7.0"
    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics")

    val daggerVersion = "2.50"
    val hiltVersion = "1.1.0"
    //Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:$daggerVersion")
    kapt ("com.google.dagger:hilt-compiler:$daggerVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:$hiltVersion")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
}