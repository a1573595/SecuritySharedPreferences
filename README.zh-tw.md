*其他語言版本: [English](README.md), [中文](README.zh-tw.md).*

# SecuritySharedPreferences
小型的Android kotlin SharedPreferences封裝加密函式庫。

⚡ Kotlin.

🚀 簡易使用。

🔒 保護資料 (Android KeyStore + AES GCM / + RSA ECB).

## 支援Android版本
- Android 4.4 Lollipop(API level 19)或更高。

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

## 用法
定義你的SecuritySharedPreferences並添加要存儲的參數。
```
class DefaultPreferences(context: Context) : SecuritySharedPreferences(context) {
    // 在此處添加您存儲的參數。
    var email by PreferencesData<String>("EMAIL", "")
}
```

或者你可以自定義KeyStore別名、SharedPreferences名稱和模式。
```
class CustomPreferences(context: Context) :
    SecuritySharedPreferences(context, "Custom", "${context.packageName}.custom") {
    // 在此處添加您存儲的參數。
}
```

初始化SecuritySharedPreferences實體，簡單的取得與儲存數值。

```
val preferencesManager = DefaultPreferences(this)

// 從SecuritySharedPreferences讀取數值
val email = preferencesManager.email

// 儲存數值到SecuritySharedPreferences
preferencesManager.email = "ABC@gmail.com"
```
