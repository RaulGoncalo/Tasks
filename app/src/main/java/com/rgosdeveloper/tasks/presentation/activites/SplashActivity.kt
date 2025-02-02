package com.rgosdeveloper.tasks.presentation.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.databinding.ActivitySplashBinding
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.presentation.viewmodel.UserPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private lateinit var userPreferencesViewModel: UserPreferencesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userPreferencesViewModel = ViewModelProvider(this)[UserPreferencesViewModel::class.java]

        userPreferencesViewModel.userPreferences.observe(this){ state ->
            when(state) {
                is ResultState.Success -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is ResultState.Error -> {
                    startActivity(Intent(this, SigninActivity::class.java))
                }
                is ResultState.Loading -> {
                    binding.ProgressBar.visibility = View.VISIBLE
                }
            }
        }

        userPreferencesViewModel.getUserPreferences()
    }
}