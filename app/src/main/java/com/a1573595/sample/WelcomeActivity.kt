package com.a1573595.sample

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Transition
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import com.a1573595.sample.databinding.ActivityWelcomeBinding
import com.a1573595.sample.tool.DefaultPreferences

class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations()
        }

        val preferencesManager = DefaultPreferences(this)
        viewBinding.tvEmail.text = getString(R.string.hi_name, preferencesManager.email)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupWindowAnimations() {
        val enterTransition = ChangeBounds()
        enterTransition.duration = 1000
        enterTransition.interpolator = DecelerateInterpolator()
        enterTransition.addListener(enterListener)
        window.sharedElementEnterTransition = enterTransition
    }

    private var enterListener: Transition.TransitionListener =
        object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {}
            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)

                val animation = AnimationUtils.loadAnimation(
                    this@WelcomeActivity,
                    R.anim.zoom_in
                )
                animation.repeatCount = Animation.INFINITE
                animation.repeatMode = Animation.REVERSE
                animation.duration = 500
                animation.fillAfter = false
                animation.startOffset = 250
                viewBinding.imgDuck.startAnimation(animation)
            }

            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionResume(transition: Transition) {}
        }
}
