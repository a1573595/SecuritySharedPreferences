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

## 使用
```
// 使用默認的SecuritySharedPreferences
class DefaultPreferences(context: Context) : SecuritySharedPreferences(context) {
    // 添加存儲的參數...
    var email by PreferencesData<String>("EMAIL", "")
}

// 自定義SecuritySharedPreferences
class CustomPreferences(context: Context) :
    SecuritySharedPreferences(context, "Custom", "${context.packageName}.custom") {
    // 添加存儲的參數...
}
```

```
// 初始化SecuritySharedPreferences實體
val preferencesManager = DefaultPreferences(this)
// 從SecuritySharedPreferences讀取數值
val email = preferencesManager.email
// 儲存數值到SecuritySharedPreferences
preferencesManager.email = "ABC@gmail.com"
```
