plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	id("com.google.gms.google-services")
    id("kotlin-parcelize")


}

android {
	namespace = "com.example.rmviewer"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.example.rmviewer"
		minSdk = 24
		targetSdk = 36
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
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}

	buildFeatures {
		viewBinding = true
	}

}

dependencies {
	implementation("androidx.navigation:navigation-fragment-ktx:2.9.6")
	implementation("androidx.navigation:navigation-ui-ktx:2.9.5")
	// Retrofit principal
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	// Convertidor Gson para JSON
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.androidx.activity)
	implementation(libs.androidx.constraintlayout)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	// Import the Firebase BoM
	implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
	implementation("com.google.firebase:firebase-analytics")
	implementation("com.google.firebase:firebase-database:20.3.0")
	implementation("com.google.firebase:firebase-auth-ktx:22.2.0")
	implementation("com.google.firebase:firebase-database-ktx:20.3.1")






}