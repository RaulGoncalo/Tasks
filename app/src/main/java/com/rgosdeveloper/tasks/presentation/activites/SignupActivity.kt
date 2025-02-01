package com.rgosdeveloper.tasks.presentation.activites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.databinding.ActivitySigninBinding
import com.rgosdeveloper.tasks.databinding.ActivitySignupBinding
import com.rgosdeveloper.tasks.domain.UserModel
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.common.ResultValidate
import com.rgosdeveloper.tasks.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderScriptBlur

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setObservers()


        initViews()
    }

    private fun setObservers() {
        authViewModel.signUpState.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    Toast.makeText(this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                    hideLoading()
                }

                is ResultState.Error -> {
                    Toast.makeText(this, it.exception.message, Toast.LENGTH_SHORT).show()
                    hideLoading()
                }

                is ResultState.Loading -> showLoading()
            }
        }

        authViewModel.nameState.observe(this) {
            if (it is ResultValidate.Error) {
                binding.editTxtLayoutName.error = it.message
            } else {
                binding.editTxtLayoutName.error = null
            }
        }

        authViewModel.emailState.observe(this) {
            if (it is ResultValidate.Error) {
                binding.editTxtLayoutEmail.error = it.message
            } else {
                binding.editTxtLayoutEmail.error = null
            }
        }

        authViewModel.passwordState.observe(this) {
            if (it is ResultValidate.Error) {
                binding.editTxtLayoutPassword.error = it.message
            } else {
                binding.editTxtLayoutPassword.error = null
            }
        }
    }

    private fun hideLoading() {
        binding.loadingState.visibility = View.GONE
    }

    private fun showLoading() {
        binding.loadingState.visibility = View.VISIBLE
    }

    private fun initViews() {
        with(binding) {
            imgBtnBack.setOnClickListener {
                finish()
            }

            editTxtName.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    editTxtLayoutName.error = null
                    editTxtName.error = null
                }
            }

            editTxtEmail.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    editTxtLayoutEmail.error = null
                    editTxtEmail.error = null
                }
            }

            editTxtPassword.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    editTxtLayoutPassword.error = null
                    editTxtPassword.error = null
                }
            }

            btnSignUp.setOnClickListener {
                val name = editTxtName.text.toString()
                val email = editTxtEmail.text.toString()
                val password = editTxtPassword.text.toString()

                val user = UserModel(
                    token = "", name = name, email = email, password = password
                )

                hideKeyboardAndClearFocus()

                authViewModel.signUp(user)
            }
        }


        bluerViewConfiguration()
    }

    private fun hideKeyboardAndClearFocus() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }

    private fun bluerViewConfiguration() {
        //Blur view config
        val radius = 10f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowsBackground = decorView.background
        binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowsBackground).setBlurRadius(radius)
        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true
    }
}