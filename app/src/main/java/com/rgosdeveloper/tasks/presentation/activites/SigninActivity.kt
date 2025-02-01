package com.rgosdeveloper.tasks.presentation.activites

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rgosdeveloper.tasks.databinding.ActivitySigninBinding
import com.rgosdeveloper.tasks.domain.UserModel
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.common.ResultValidate
import com.rgosdeveloper.tasks.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderScriptBlur

@AndroidEntryPoint
class SigninActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySigninBinding.inflate(layoutInflater)
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
        authViewModel.signInState.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    hideLoading()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is ResultState.Error -> {
                    hideLoading()
                    Toast.makeText(this, it.exception.message, Toast.LENGTH_SHORT).show()
                }

                is ResultState.Loading -> showLoading()
            }
        }

        authViewModel.emailState.observe(this) {
            if (it is ResultValidate.Error) {
                binding.editTxtLayoutEmail.error = it.message
            }else{
                binding.editTxtLayoutEmail.error = null
            }
        }

        authViewModel.passwordState.observe(this) {
            if(it is ResultValidate.Error){
                binding.editTxtLayoutPassword.error = it.message
            }else{
                binding.editTxtLayoutPassword.error = null
            }
        }
    }

    private fun showLoading() {
        binding.loadingState.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingState.visibility = View.GONE
    }

    private fun initViews() {
        with(binding){
            editTxtEmail.setText("raulgoncalo.98@gmail.com")
            editTxtPassword.setText("1234567")

            btnSignIn.setOnClickListener {
                val email = editTxtEmail.text.toString()
                val password = editTxtPassword.text.toString()

                val user = UserModel(
                    token = "",
                    name = "",
                    email = email,
                    password = password
                )
                hideKeyboardAndClearFocus()
                authViewModel.signIn(user)
            }

            editTxtPassword.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    editTxtLayoutPassword.error = null
                    editTxtPassword.error = null
                }
            }

            editTxtEmail.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus){
                    editTxtLayoutEmail.error = null
                    editTxtEmail.error = null
                }
            }

            txtLinkSignUp.setOnClickListener {
                goToSignUp()
            }

            bluerViewConfiguration()
        }
    }

    private fun goToSignUp() {
        startActivity(Intent(this, SignupActivity::class.java))
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
        val rootView = decorView.findViewById<ViewGroup>(R.id.content)
        val windowsBackground = decorView.background
        binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowsBackground)
            .setBlurRadius(radius)
        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true
    }
}