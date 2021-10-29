package com.a1573595.securitysharedpreferences

import android.content.Context

class FakePreferences(context: Context) : SecuritySharedPreferences(context, context.packageName) {
    var doubleValue by PreferencesData<Double>("Double", 0.0)

    var floatValue by PreferencesData<Float>("Float", 0f)

    var longValue by PreferencesData<Long>("Long", 0L)

    var intValue by PreferencesData<Int>("Int", 0)

    var charValue by PreferencesData<Char>("Char", Character.MIN_VALUE)

    var shortValue by PreferencesData<Short>("Short", 0)

    var byteValue by PreferencesData<Byte>("Byte", 0)

    var booleanValue by PreferencesData<Boolean>("Boolean", false)

    var stringValue by PreferencesData<String>("String", "")
}