package com.a1573595.sample

import android.app.Application
import androidx.lifecycle.*

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = DefaultPreferences(application)

    val account: MutableLiveData<Event<String?>> = MutableLiveData()

    val accountError: MutableLiveData<Event<String?>> = MutableLiveData()
    val passwordError: MutableLiveData<Event<String?>> = MutableLiveData()

    val loginSuccess: MutableLiveData<Event<Boolean>> = MutableLiveData()

    init {
        account.postValue(Event(preferencesManager.account))
    }

    fun clear() {
        preferencesManager.clear()
    }

    fun login(account: String, password: String) {
        accountError.postValue(Event(null))
        passwordError.postValue(Event(null))

        when {
            account.isBlank() -> accountError.postValue(Event(getString(R.string.account_cannot_be_empty)))
            password.isBlank() -> passwordError.postValue(Event(getString(R.string.password_cannot_be_empty)))
            else -> {
                preferencesManager.account = account
                loginSuccess.postValue(Event(true))
            }
        }
    }

    private fun getString(resId: Int, vararg any: Any?) =
        getApplication<Application>().getString(resId, any)
}