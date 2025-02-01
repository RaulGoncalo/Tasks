package com.rgosdeveloper.tasks.presentation.activites

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.databinding.ActivitySigninBinding
import com.rgosdeveloper.tasks.databinding.ActivitySignupBinding
import eightbitlab.com.blurview.RenderScriptBlur

class SignupActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

       initViews()
    }

    private fun initViews() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        bluerViewConfiguration()
    }

    private fun bluerViewConfiguration() {
        //Blur view config
        val radius = 10f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowsBackground = decorView.background
        binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowsBackground)
            .setBlurRadius(radius)
        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true
    }
}