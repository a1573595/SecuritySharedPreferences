package com.a1573595.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.animation.DecelerateInterpolator
import com.a1573595.sample.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setupWindowAnimations()

        val preferencesManager = DefaultPreferences(this)
        viewBinding.tvEmail.text = getString(R.string.hi_name, preferencesManager.account)
    }

    private fun setupWindowAnimations() {
        val enterTransition = ChangeBounds()
        enterTransition.duration = 1000
        enterTransition.interpolator = DecelerateInterpolator()
        window.sharedElementEnterTransition = enterTransition
    }
}
