*Read this in other languages: [English](README.md), [中文](README.zh-tw.md).*

# SecuritySharedPreferences
A small SharedPreferences wrapper and cryptographic android library power by Kotlin.

⚡ Kotlin powered.

🚀 Easy to use.

🔒 Protect data (Android KeyStore + AES GCM / + RSA ECB).

## Difference
Difference between the android SharedPreference and SecuritySharedPreferences library.
```
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="EMAIL">ABC@gmail.com</string>
</map>
```

```
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="72d90af393d610727bdf1fa58d1010c116f263ae7ce096eeb7eb36c2ae646791">7sfn2pm7ueQ7AU5UrT4f8gYJryGsT16ZT/sHhkI=&#10;    </string>
</map>
```

## Supported Android Versions
- Android 4.4 Lollipop(API level 19) or higher.

## Gradle
```groovy
allprojects {
    repositories {
    ...
    
    maven { url 'https://jitpack.io' }
    }
}
```

```groovy
dependencies {
    implementation 'com.github.a1573595:SecuritySharedPreferences:1.0.0'
}
```

## Usage
Define your SecuritySharedPreferences and add the parameters to be stored.
```kotlin
class DefaultPreferences(context: Context) : SecuritySharedPreferences(context) {
    // Add your stored parameter in here.
    var email by PreferencesData<String>("EMAIL", "")
}
```

Or you can customize the KeyStore alias, SharedPreferences name and mode.
```kotlin
class CustomPreferences(context: Context) :
    SecuritySharedPreferences(context, "Custom", "${context.packageName}.custom") {
    // Add your stored parameter...
}
```

Initialization SecuritySharedPreferences instance, easy to get and set stored value.
```kotlin
val preferencesManager = DefaultPreferences(this)

// get value from SecuritySharedPreferences
val email = preferencesManager.email

// save value into SecuritySharedPreferences
preferencesManager.email = "ABC@gmail.com"
```

## Reference
[AndroidKeyStore](https://github.com/joetsaitw/AndroidKeyStore)
