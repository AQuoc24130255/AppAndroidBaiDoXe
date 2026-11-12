plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Kích hoạt plugin Google Services
}

android {
    namespace = "com.example.appandroidbaidoxe"
    compileSdk {
        version = release(37)
    }

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.appandroidbaidoxe"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    // Quản lý đồng bộ phiên bản bằng Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:35.1.0"))

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    // 1. Xác thực người dùng (Đăng nhập / Đăng ký)
    implementation("com.google.firebase:firebase-auth")

    // 2. Cơ sở dữ liệu thời gian thực (Cập nhật trạng thái chỗ đỗ xe tức thì)
    implementation("com.google.firebase:firebase-database")

    // 3. (Tùy chọn) Firestore Database nếu muốn truy vấn dữ liệu phức tạp hơn
    // implementation("com.google.firebase:firebase-firestore")
}