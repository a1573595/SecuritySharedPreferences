package com.a1573595.securitysharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.zip.DataFormatException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class SecuritySharedPreferences(
    context: Context,
    keyAlias: String,
    preferenceName: String = keyAlias
) {
    private val sp: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context, keyAlias)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sp = EncryptedSharedPreferences.create(
            context,
            preferenceName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun clear(): Boolean = sp.edit().clear().commit()

    protected inner class PreferencesData<T>(private val key: String, private val defaultValue: T) :
        ReadWriteProperty<Any?, T> {
        init {
            if (defaultValue !is Boolean && defaultValue !is Number && defaultValue !is Char && defaultValue !is String) {
                throw(DataFormatException("Unsupported data type, only support Boolean, Number, Char and String."))
            }
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            sp.edit(true) { putString(key, value?.toString()) }
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            val encryptStr: String? = sp.getString(key, null)

            return if (encryptStr.isNullOrEmpty()) {
                defaultValue
            } else {
                when (defaultValue) {
                    is Double -> encryptStr.toDouble()
                    is Float -> encryptStr.toFloat()
                    is Long -> encryptStr.toLong()
                    is Int -> encryptStr.toInt()
                    is Char -> encryptStr.single()
                    is Short -> encryptStr.toShort()
                    is Byte -> encryptStr.toByte()
                    is Boolean -> encryptStr.toBoolean()
                    else -> encryptStr
                } as T
            }
        }
    }
}