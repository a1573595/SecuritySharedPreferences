package com.a1573595.sample

import com.a1573595.securitysharedpreferences.FakeAndroidKeyStore

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultPreferencesTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeAndroidKeyStore.setup
        }
    }

    private val preferences: DefaultPreferences =
        DefaultPreferences(ApplicationProvider.getApplicationContext())

    @Test
    fun verify_name_storage() {
        val value = "Chien"

        preferences.userName = value

        Assert.assertEquals(value, preferences.userName)
    }

    @Test
    fun verify_account_storage() {
        val value = "a1573595@gmail.com"

        preferences.account = value

        Assert.assertEquals(value, preferences.account)
    }

    @Test
    fun verify_address_storage() {
        val value = "Taiwan"

        preferences.address = value

        Assert.assertEquals(value, preferences.address)
    }

    @Test
    fun verify_age_storage() {
        val value = 27

        preferences.age = value

        Assert.assertEquals(value, preferences.age)
    }


    @Test
    fun verify_height_storage() {
        val value = 180f

        preferences.height = value

        Assert.assertEquals(value, preferences.height)
    }
}