*Read this in other languages: [English](README.md), [中文](README.zh-tw.md).*

# SecuritySharedPreferences
A small SharedPreferences wrapper and cryptographic android library power by Kotlin.

⚡ Kotlin powered.

🚀 Easy to use.

🔒 Protect data (Android KeyStore + AES256 SIV / + AES256 GCM).

🧪 Support Unit test.

## Difference
Difference between the android SharedPreference and SecuritySharedPreferences library.
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

## Supported Android Versions
- Android 5.1 Lollipop(API level 21) or higher.

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

## Usage
Define your SecuritySharedPreferences and add the parameters to be stored.
```kotlin
class DefaultPreferences(context: Context) :
    SecuritySharedPreferences(context, context.packageName) {
    // Add your stored parameter...
    var userName by PreferencesData<String>("Name", "Chien")

    var email by PreferencesData<String>("Email", "")

    var address by PreferencesData<String>("Address", "")

    var age by PreferencesData<Int>("Age", 25)

    var height by PreferencesData<Float>("Height", 1.75f)
}
```

Or you can customize the KeyStore alias, SharedPreferences name and mode.
```kotlin
class CustomPreferences(context: Context) :
    SecuritySharedPreferences(context, "${context.packageName}.custom") {
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

## Reference
[AndroidKeyStore](https://github.com/joetsaitw/AndroidKeyStore)

[Testing Jetpack Security with Robolectric](https://proandroiddev.com/testing-jetpack-security-with-robolectric-9f9cf2aa4f61)
