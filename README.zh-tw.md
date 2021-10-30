*其他語言版本: [English](README.md), [中文](README.zh-tw.md).*

# SecuritySharedPreferences
小型的Android kotlin SharedPreferences封裝加密函式庫。

⚡ Kotlin。

🚀 簡易使用。

🔒 保護資料 (Android KeyStore + AES256 SIV / + AES256 GCM)。

🧪 支援Unit test。

## 差異
Android SharedPreference與SecuritySharedPreferences函式庫差異。
* SharedPreference
```
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="Account">ABC@gmail.com</string>
</map>
```

* SecuritySharedPreferences
```
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="AWT/LySwiIhTyjEtGKZ9kQ0t6n30XnNbLft3/g==">AVQneV9aILgdLAT+OYJowJYWzeRktEj7gsttnTN4bLXMa690QKYnBWq1MuwnFpYAhjV/Gna2axuvqw==</string>
</map>
```

## 支援Android版本
- Android 5.1 Lollipop(API level 21)或更高。

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
    implementation 'com.github.a1573595:SecuritySharedPreferences:2.0.3'
	
    // Optional - Unit test
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'androidx.arch.core:core-testing:2.1.0'
    testImplementation 'androidx.test:core-ktx:1.4.0'
    testImplementation 'androidx.test.ext:junit-ktx:1.1.3'
    testImplementation 'org.robolectric:robolectric:4.6.1'
}
```

## 用法
定義你的SecuritySharedPreferences並添加要存儲的參數。
```kotlin
class DefaultPreferences(context: Context) :
    SecuritySharedPreferences(context, context.packageName) {
    // 在此處添加您存儲的參數...
    var userName by PreferencesData<String>("Name", "Chien")

    var email by PreferencesData<String>("Email", "")

    var address by PreferencesData<String>("Address", "")

    var age by PreferencesData<Int>("Age", 25)

    var height by PreferencesData<Float>("Height", 1.75f)
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

## Unit test
```kotlin
@RunWith(AndroidJUnit4::class)
class SecuritySharedPreferencesTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeAndroidKeyStore.setup
        }
    }

    private val preferences: CustomPreferences =
        CustomPreferences(ApplicationProvider.getApplicationContext())

	...
}
```

## 參考
[AndroidKeyStore](https://github.com/joetsaitw/AndroidKeyStore)

[Testing Jetpack Security with Robolectric](https://proandroiddev.com/testing-jetpack-security-with-robolectric-9f9cf2aa4f61)