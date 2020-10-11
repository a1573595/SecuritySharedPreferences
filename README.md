*Read this in other languages: [English](README.md), [中文](README.zh-tw.md).*

# SecuritySharedPreferences
A small SharedPreferences wrapper and cryptographic android library power by Kotlin.

⚡ Kotlin powered.

🚀 Easy to use.

🔒 Protect data (Android KeyStore + AES GCM / + RSA ECB).

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
// init SecuritySharedPreferences instance
val preferencesManager = DefaultPreferences(this)
// get value from SecuritySharedPreferences
val email = preferencesManager.email
// save value into SecuritySharedPreferences
preferencesManager.email = "ABC@gmail.com"
```
