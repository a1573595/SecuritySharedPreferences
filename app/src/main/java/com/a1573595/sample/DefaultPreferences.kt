package com.a1573595.sample

import android.content.Context
import com.a1573595.securitysharedpreferences.SecuritySharedPreferences

/**
 * Define your SecuritySharedPreferences and add the parameters to be stored.
 * Or you can customize the KeyStore alias, SharedPreferences name and mode.
 *
 * Customize the stored parameter name, type, and default value like
 * var value by PreferencesData<T>("KEY", DEFAULT_VALUE)
 * Example:
 * var str by PreferencesData<String>("KEY_STR", "")
 * var nullableStr by PreferencesData<String?>("KEY_NULLABLE_STR", null)
 */

// Use default SecuritySharedPreferences
class DefaultPreferences(context: Context) :
    SecuritySharedPreferences(context, context.packageName) {
    // Add your stored parameter...
    var userName by PreferencesData<String>("Name", "Chien")

    var account by PreferencesData<String>("Account", "")

    var address by PreferencesData<String>("Address", "")

    var age by PreferencesData<Int>("Age", 25)

    var height by PreferencesData<Float>("Height", 1.75f)
}

// Customize your SecuritySharedPreferences
class CustomPreferences(context: Context) :
    SecuritySharedPreferences(context, "${context.packageName}.custom") {
    // Add your stored parameter...
}