package com.a1573595.sample

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.a1573595.securitysharedpreferences.FakeAndroidKeyStore
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginViewModelTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeAndroidKeyStore.setup
        }
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val account = "account"
    private val password = "password"

    private val application: Application = ApplicationProvider.getApplicationContext()
    private val viewModel: LoginViewModel =
        LoginViewModel(ApplicationProvider.getApplicationContext())

    @Test
    fun login_without_account() {
        viewModel.login("", password)

        Assert.assertEquals(
            application.getString(R.string.account_cannot_be_empty),
            viewModel.accountError.value?.peekContent()
        )
    }

    @Test
    fun login_without_password() {
        viewModel.login(account, "")

        Assert.assertEquals(
            application.getString(R.string.password_cannot_be_empty),
            viewModel.passwordError.value?.peekContent()
        )
    }

    @Test
    fun login_success() {
        viewModel.login(account, password)

        Assert.assertEquals(
            true,
            viewModel.loginSuccess.value?.peekContent()
        )
    }
}