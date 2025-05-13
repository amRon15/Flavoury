plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.flavoury"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.flavoury"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.squareup.picasso:picasso:2.8")
    implementation ("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-base:18.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google:google:5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.material:material:1.13.0-alpha03")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
}