*其他語言版本: [English](README.md), [中文](README.zh-tw.md).*

# SecuritySharedPreferences
小型的Android kotlin SharedPreferences封裝加密函式庫。

⚡ Kotlin.

🚀 簡易使用。

🔒 保護資料 (Android KeyStore + AES GCM / + RSA ECB).

## 差異
Android SharedPreference與SecuritySharedPreferences函式庫差異。
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

## 支援Android版本
- Android 4.4 Lollipop(API level 19)或更高。

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
    implementation 'com.github.a1573595:SecuritySharedPreferences:1.1.0'
}
```

## 用法
定義你的SecuritySharedPreferences並添加要存儲的參數。
```kotlin
class DefaultPreferences(context: Context) :
    SecuritySharedPreferences(context, context.packageName) {
    // 在此處添加您存儲的參數...
    var userName by PreferencesData<String>("Name", "")

    var email by PreferencesData<String>("Email", "")

    var address by PreferencesData<String>("Address", "")

    var age by PreferencesData<Int>("Age", 25)

    var height by PreferencesData<Float>("Age", 1.75f)
}
```

或者你可以自定義KeyStore別名、SharedPreferences名稱和模式。
```kotlin
class CustomPreferences(context: Context) :
    SecuritySharedPreferences(context, "${context.packageName}.custom") {
    // 在此處添加您存儲的參數...
}
```

初始化SecuritySharedPreferences實體，簡單的取得與儲存數值。

```kotlin
val preferencesManager = DefaultPreferences(this)

// 從SecuritySharedPreferences讀取數值
val email = preferencesManager.email

// 儲存數值到SecuritySharedPreferences
preferencesManager.email = "ABC@gmail.com"
```

## 參考
[AndroidKeyStore](https://github.com/joetsaitw/AndroidKeyStore)