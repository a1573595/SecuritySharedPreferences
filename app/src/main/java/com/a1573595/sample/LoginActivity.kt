package com.a1573595.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.a1573595.sample.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        subscriptViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_clear, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_clear) {
            viewModel.clear()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        binding.btnLogin.setOnClickListener {
            viewModel.login("${binding.tfAccount.text}", "${binding.tfPassword.text}")
        }
    }

    private fun subscriptViewModel() {
        viewModel.account.observe(this, EventObserver {
            binding.tfAccount.setText(it)
        })

        viewModel.accountError.observe(this, EventObserver {
            binding.accountLayout.error = it
        })

        viewModel.passwordError.observe(this, EventObserver {
            binding.passwordLayout.error = it
        })

        viewModel.loginSuccess.observe(this, EventObserver {
            val p1: Pair<View, String> =
                Pair.create(binding.tfAccount, binding.tfAccount.transitionName)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1)

            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent, options.toBundle())
        })
    }
}
