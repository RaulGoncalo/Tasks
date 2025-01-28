package com.rgosdeveloper.tasks.presentation.activites

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.databinding.ActivitySigninBinding
import com.rgosdeveloper.tasks.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}