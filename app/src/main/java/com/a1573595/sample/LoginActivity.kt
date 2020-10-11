package com.a1573595.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.a1573595.sample.tool.DefaultPreferences
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val preferencesManager = DefaultPreferences(this)
        // reset value
//        preferencesManager.clear()

        edEmail.setText(preferencesManager.email)

        btnLogin.setOnClickListener {
            // save value to sharedPreference
            if (edEmail.length() > 0) {
                preferencesManager.email = edEmail.text.toString()
            }

            val p1: Pair<View, String> =
                    Pair.create<View, String>(edEmail, "tvEmail")
            val p2: Pair<View, String> =
                    Pair.create<View, String>(imgDuck, "imgDuck")

            val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2)

            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent, options.toBundle())
        }
    }
}
