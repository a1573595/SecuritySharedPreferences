package com.a1573595.securitysharedpreferences

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecuritySharedPreferencesTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeAndroidKeyStore.setup
        }
    }

    private val preferences: FakePreferences =
        FakePreferences(ApplicationProvider.getApplicationContext())

    @Test
    fun verify_double_storage() {
        val value = 3.1415927

        preferences.doubleValue = value

        Assert.assertEquals(value, preferences.doubleValue, 0.0)
    }

    @Test
    fun verify_float_storage() {
        val value = 3.1415927f

        preferences.floatValue = value

        Assert.assertEquals(value, preferences.floatValue)
    }

    @Test
    fun verify_long_storage() {
        val value = Int.MAX_VALUE.toLong() + 1

        preferences.longValue = value

        Assert.assertEquals(value, preferences.longValue)
    }

    @Test
    fun verify_int_storage() {
        val value = 123

        preferences.intValue = value

        Assert.assertEquals(value, preferences.intValue)
    }


    @Test
    fun verify_char_storage() {
        val value: Char = ' '

        preferences.charValue = value

        Assert.assertEquals(value, preferences.charValue)
    }

    @Test
    fun verify_short_storage() {
        val value: Short = 125

        preferences.shortValue = value

        Assert.assertEquals(value, preferences.shortValue)
    }

    @Test
    fun verify_byte_storage() {
        val value:Byte = 0xFF.toByte()

        preferences.byteValue = value

        Assert.assertEquals(value, preferences.byteValue)
    }

    @Test
    fun verify_boolean_storage() {
        val value = true

        preferences.booleanValue = value

        Assert.assertEquals(value, preferences.booleanValue)
    }

    @Test
    fun verify_string_storage() {
        val value = "value"

        preferences.stringValue = value

        Assert.assertEquals(value, preferences.stringValue)
    }
}