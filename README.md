*Read this in other languages: [English](README.md), [中文](README.zh-tw.md).*

# SecuritySharedPreferences
Android encrypted SharedPreferences.

## Supported Android Versions
- Android 4.4 Lollipop(API level 19) or higher.


## Gradle
```
allprojects {
    repositories {
    ...
    
    maven { url 'https://jitpack.io' }
    }
}
```

```
android {
    ...
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation 'com.github.a1573595:SecuritySharedPreferences:1.0.0'
}
```

## Usage
```
// Use default SecuritySharedPreferences
class DefaultPreferences(context: Context) : SecuritySharedPreferences(context) {
    // Add your stored parameter...
    var email by PreferencesData<String>("EMAIL", "")
}

// Customize your SecuritySharedPreferences
class CustomPreferences(context: Context) :
    SecuritySharedPreferences(context, "Custom", "${context.packageName}.custom") {
    // Add your stored parameter...
}
```

```
// init SharedPreferences instance
val preferencesManager = DefaultPreferences(this)
// get value from SharedPreferences
val email = preferencesManager.email
// set value to SharedPreferences
preferencesManager.email = "ABC@gmail.com"
```
