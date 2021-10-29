package com.a1573595.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.a1573595.sample.databinding.ActivityLoginBinding
import com.a1573595.sample.tool.DefaultPreferences

class LoginActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val preferencesManager = DefaultPreferences(this)
        // reset value
//        preferencesManager.clear()

        viewBinding.tfAccount.setText(preferencesManager.email)

        viewBinding.btnLogin.setOnClickListener {
            // save value to sharedPreference
            if (viewBinding.tfAccount.length() > 0) {
                preferencesManager.email = viewBinding.tfAccount.text.toString()
            }

            val p1: Pair<View, String> =
                Pair.create(viewBinding.tfAccount, viewBinding.tfAccount.transitionName)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1)

            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent, options.toBundle())
        }
    }
}
