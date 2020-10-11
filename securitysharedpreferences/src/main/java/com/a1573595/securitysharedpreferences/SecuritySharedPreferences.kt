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
    keyAlias: String = "Alias",
    name: String = context.packageName,
    mode: Int = Context.MODE_PRIVATE
) {
    private val KEYSTORE_PROVIDER = "AndroidKeyStore"
    private val KEYSTORE_ALIAS = keyAlias
    private val RSA_MODE = "RSA/ECB/PKCS1Padding"
    private val AES_MODE = "AES/GCM/NoPadding"

    private val PREF_KEY_AES = "PREF_KEY_AES"
    private val PREF_KEY_IV = "PREF_KEY_IV"

    private val sp: SharedPreferences = context.getSharedPreferences(name, mode)

    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)

    init {
        keyStore.load(null)
        if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
            clear()

            genKeyStoreKey(context)
            genAESKey()
        }
    }

    // Clear data except AES keys
    fun clear() {
        val editor = sp.edit()

        val strAES = PREF_KEY_AES.toSHA256()
        val strIV = PREF_KEY_IV.toSHA256()

        for (key in sp.all.keys) {
            if (key == strAES || key == strIV) {
                continue
            }

            editor.remove(key)
        }

        editor.apply()
    }

    fun String.toSHA256(): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(this.toByteArray(charset("UTF-8")))
            val hexString = StringBuffer()
            for (i in hash.indices) {
                val hex = Integer.toHexString(0xff and hash[i].toInt())
                if (hex.length == 1) hexString.append('0')
                hexString.append(hex)
            }
            hexString.toString()
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
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
        val publicKey =
            keyStore.getCertificate(KEYSTORE_ALIAS)
                .publicKey
        val cipher = Cipher.getInstance(RSA_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        val encryptedByte = cipher.doFinal(plainText)
        return Base64.encodeToString(encryptedByte, Base64.DEFAULT)
    }

    private fun decryptRSA(encryptedText: String): ByteArray {
        val privateKey = keyStore.getKey(
            KEYSTORE_ALIAS,
            null
        ) as PrivateKey
        val cipher = Cipher.getInstance(RSA_MODE)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        val encryptedBytes =
            Base64.decode(encryptedText, Base64.DEFAULT)
        return cipher.doFinal(encryptedBytes)
    }

    private fun encryptAES(plainText: String): String {
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            getAESKey(),
            IvParameterSpec(getIV())
        )

        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    private fun decryptAES(encryptedText: String): String {
        val decodedBytes =
            Base64.decode(encryptedText.toByteArray(), Base64.DEFAULT)

        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(
            Cipher.DECRYPT_MODE,
            getAESKey(),
            IvParameterSpec(getIV())
        )

        return String(cipher.doFinal(decodedBytes))
    }

    private fun setAESKey(aesKey: ByteArray) {
        sp.edit(true) { putString(PREF_KEY_AES.toSHA256(), encryptRSA(aesKey)) }
    }

    private fun getAESKey(): SecretKeySpec {
        val encryptedKey: String = sp.getString(PREF_KEY_AES.toSHA256(), "") as String
        val aesKey = decryptRSA(encryptedKey)

        return SecretKeySpec(aesKey, AES_MODE)
    }

    private fun setIV(iv: ByteArray) {
        sp.edit(true) { putString(PREF_KEY_IV.toSHA256(), encryptRSA(iv)) }
    }

    private fun getIV(): ByteArray {
        val encryptedIV: String = sp.getString(PREF_KEY_AES.toSHA256(), "") as String
        return decryptRSA(encryptedIV)
    }

    protected inner class PreferencesData<T>(private val key: String, private val defaultValue: T) :
        ReadWriteProperty<Any?, T> {
        init {
            when (defaultValue) {
                is Number, is String -> {
                }
                else -> throw(DataFormatException("Unsupported data type, only support Number and String"))
            }
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            sp.edit { putString(key.toSHA256(), encryptAES(value.toString())) }
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            val encryptStr = sp.getString(key.toSHA256(), null)

            return if (encryptStr == null) {
                defaultValue
            } else {
                val value = decryptAES(encryptStr)
                value as T
            }
        }
    }
}
