package com.a1573595.securitysharedpreferences

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import java.math.BigInteger
import java.security.*
import java.util.*
import java.util.zip.DataFormatException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.x500.X500Principal
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class SecuritySharedPreferences(
    context: Context,
    name: String,
    mode: Int = Context.MODE_PRIVATE
) {
    private val KEYSTORE_PROVIDER = "AndroidKeyStore"
    private val KEYSTORE_ALIAS = name
    private val RSA_MODE = "RSA/ECB/PKCS1Padding"
    private val AES_MODE = "AES/GCM/NoPadding"

    private val PREF_KEY_AES = "PREF_KEY_AES"
    private val PREF_KEY_IV = "PREF_KEY_IV"

    private val sp: SharedPreferences =
        context.getSharedPreferences(name, mode)

    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)

    private val aesKey: SecretKeySpec
    private val ivParameterSpec: IvParameterSpec

    init {
        keyStore.load(null)

        if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
            sp.edit().clear().commit()

            genKeyStoreKey(context)
            genAESKey()
        }

        aesKey = getAESKey()
        ivParameterSpec = IvParameterSpec(getIV())
    }

    // Clear data except AES keys
    fun clear(): Boolean {
        val editor = sp.edit()

        val strAES = PREF_KEY_AES.toSHA1()
        val strIV = PREF_KEY_IV.toSHA1()

        for (key in sp.all.keys) {
            if (key == strAES || key == strIV) {
                continue
            }

            editor.remove(key)
        }

        return editor.commit()
    }

    private fun genKeyStoreKey(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            generateRSAKeyAboveApi23()
        } else {
            generateRSAKeyBelowApi23(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateRSAKeyAboveApi23() {
        val keyPairGenerator: KeyPairGenerator = KeyPairGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEYSTORE_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .build()
        keyPairGenerator.initialize(keyGenParameterSpec)
        keyPairGenerator.generateKeyPair()
    }

    private fun generateRSAKeyBelowApi23(context: Context) {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 30)
        val spec = KeyPairGeneratorSpec.Builder(context)
            .setAlias(KEYSTORE_ALIAS)
            .setSubject(X500Principal("CN=$KEYSTORE_ALIAS"))
            .setSerialNumber(BigInteger.TEN)
            .setStartDate(start.time)
            .setEndDate(end.time)
            .build()
        val keyPairGenerator: KeyPairGenerator = KeyPairGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER)
        keyPairGenerator.initialize(spec)
        keyPairGenerator.generateKeyPair()
    }

    private fun genAESKey() {
        val aesKey = ByteArray(16)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(aesKey)

        setAESKey(aesKey)

        val generatedIV = secureRandom.generateSeed(12)
        setIV(generatedIV)
    }

    private fun encryptRSA(plainText: ByteArray): String {
        val publicKey = keyStore.getCertificate(KEYSTORE_ALIAS).publicKey

        synchronized(this) {
            val cipher = Cipher.getInstance(RSA_MODE)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)

            val encryptedByte = cipher.doFinal(plainText)
            return Base64.encodeToString(encryptedByte, Base64.DEFAULT)
        }
    }

    private fun decryptRSA(encryptedText: String): ByteArray {
        val privateKey = keyStore.getKey(KEYSTORE_ALIAS, null) as PrivateKey

        synchronized(this) {
            val cipher = Cipher.getInstance(RSA_MODE)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)

            val encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
            return cipher.doFinal(encryptedBytes)
        }
    }

    private fun encryptAES(plainText: String): String {
        synchronized(this) {
            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(
                Cipher.ENCRYPT_MODE,
                aesKey,
                ivParameterSpec
            )

            val encryptedBytes = cipher.doFinal(plainText.toByteArray())
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        }
    }

    private fun decryptAES(encryptedText: String): String {
        synchronized(this) {
            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(
                Cipher.DECRYPT_MODE,
                aesKey,
                ivParameterSpec
            )

            val decodedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
            return String(cipher.doFinal(decodedBytes))
        }
    }

    private fun setAESKey(aesKey: ByteArray) {
        sp.edit(true) { putString(PREF_KEY_AES.toSHA1(), encryptRSA(aesKey)) }
    }

    private fun getAESKey(): SecretKeySpec {
        val encryptedKey: String = sp.getString(PREF_KEY_AES.toSHA1(), "") as String
        val aesKey = decryptRSA(encryptedKey)

        return SecretKeySpec(aesKey, AES_MODE)
    }

    private fun setIV(iv: ByteArray) {
        sp.edit(true) { putString(PREF_KEY_IV.toSHA1(), encryptRSA(iv)) }
    }

    private fun getIV(): ByteArray {
        val encryptedIV: String = sp.getString(PREF_KEY_AES.toSHA1(), "") as String
        return decryptRSA(encryptedIV)
    }

    protected inner class PreferencesData<T>(private val key: String, private val defaultValue: T) :
        ReadWriteProperty<Any?, T> {
        init {
            if (defaultValue !is Boolean && defaultValue !is Number && defaultValue !is String) {
                throw(DataFormatException("Unsupported data type, only support Number and String."))
            }
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            sp.edit(true) { putString(key.toSHA1(), encryptAES(value.toString())) }
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            val encryptStr = sp.getString(key.toSHA1(), null)

            return if (encryptStr == null) {
                defaultValue
            } else {
                val value = decryptAES(encryptStr)

                when (defaultValue) {
                    is Double -> value.toDouble() as T
                    is Float -> value.toFloat() as T
                    is Long -> value.toLong() as T
                    is Int -> value.toInt() as T
                    is Char -> value.single() as T
                    is Short -> value.toShort() as T
                    is Byte -> value.toByte() as T
                    is Boolean -> value.toBoolean() as T
                    else -> value as T
                }
            }
        }
    }
}

fun String.toSHA1(): String = MessageDigest.getInstance("SHA-1")
    .digest(toByteArray())
    .fold("", { str, it -> str + "%02x".format(it) })