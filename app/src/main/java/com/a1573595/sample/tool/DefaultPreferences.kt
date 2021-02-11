package com.a1573595.sample.tool

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
class DefaultPreferences(context: Context) : SecuritySharedPreferences(context, context.packageName) {
    // Add your stored parameter...
    var email by PreferencesData<String>("EMAIL", "")
}

// Customize your SecuritySharedPreferences
class CustomPreferences(context: Context) :
    SecuritySharedPreferences(context, "${context.packageName}.custom") {
    // Add your stored parameter...
}