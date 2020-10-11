package com.a1573595.sample.tool

import android.content.Context
import com.a1573595.securitysharedpreferences.SecuritySharedPreferences

/**
 * You can use the default SecuritySharedPreferences constructor or
 * customize the value of KeyStore alias, SharedPreferences name and mode.
 *
 * Customize the stored parameter name, type, and default value like
 * var value by PreferencesData<T>("KEY", DEFAULT_VALUE)
 * Example:
 * var str by PreferencesData<String>("KEY_STR", "")
 * var nullableStr by PreferencesData<String?>("KEY_NULLABLE_STR", null)
 */

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