package com.a1573595.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a1573595.sample.databinding.ActivityWelcomeBinding
import com.a1573595.sample.tool.DefaultPreferences

class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val preferencesManager = DefaultPreferences(this)
        viewBinding.tvEmail.text = getString(R.string.hi_name, preferencesManager.email)
    }
}
