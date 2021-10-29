package com.a1573595.securitysharedpreferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptedSharedPreferencesTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeAndroidKeyStore.setup
        }
    }

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            "testPrefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    @Test
    fun verify_string_storage() {
        sharedPreferences.edit().apply {
            putString("key", "value")
        }.apply()

        assertEquals("value", sharedPreferences.getString("key", "default"))
    }
}