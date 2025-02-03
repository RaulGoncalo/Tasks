package com.rgosdeveloper.tasks.presentation.activites

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.presentation.fragments.TasksFragment
import com.rgosdeveloper.tasks.databinding.ActivityMainBinding
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.models.UserModel
import com.rgosdeveloper.tasks.presentation.viewmodel.UserPreferencesViewModel
import com.rgosdeveloper.tasks.utils.AppConstants
import com.rgosdeveloper.tasks.utils.MainConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var userPreferencesViewModel: UserPreferencesViewModel
    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userPreferencesViewModel = ViewModelProvider(this)[UserPreferencesViewModel::class.java]

        setObservers()
        userPreferencesViewModel.getUserPreferences()

        initViews()
    }

    private fun setObservers() {
        userPreferencesViewModel.userPreferences.observe(this){
            when(it){
                is ResultState.Success -> {
                    user = it.data
                    hideLoading()
                    setupNavigationView()
                }
                is ResultState.Error -> {
                    Toast.makeText(this, it.exception.message, Toast.LENGTH_SHORT).show()
                }
                is ResultState.Loading -> {
                    showLoading()
                }
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
        setWindowFlags()
        setupDrawerLayout()
        setupFab()
    }

    private fun setWindowFlags() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun setupDrawerLayout() {
        drawerLayout = binding.drawerLayout
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupNavigationView() {
        val navigationView = binding.navView
        val headerView = navigationView.getHeaderView(0)
        val backgroundHeader = headerView.findViewById<LinearLayout>(R.id.backgroundHeader)

        val userNameTxt = headerView.findViewById<TextView>(R.id.txtUserNameHeaderDrawer)
        val userEmailTxt = headerView.findViewById<TextView>(R.id.txtUserEmailHeaderDrawer)

        if(user != null){
            userNameTxt.text = user!!.name
            userEmailTxt.text = user!!.email
        }else{
            Toast.makeText(this, MainConstants.ERROR_USER_NULL, Toast.LENGTH_SHORT).show()
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationDrawerItemSelected(menuItem, backgroundHeader)
        }
        // Define o item padrão selecionado
        val defaultItem = navigationView.menu.findItem(R.id.nav_today)
        defaultItem.isChecked = true
        handleNavigationDrawerItemSelected(defaultItem, backgroundHeader)
    }

    private fun handleNavigationDrawerItemSelected(menuItem: MenuItem, backgroundHeader: LinearLayout): Boolean {
        when (menuItem.itemId) {
            R.id.nav_logout -> handleLogout()
            else -> {
                val filter = applyThemeBasedOnMenuItem(menuItem.itemId, backgroundHeader)
                openTasksFragment(filter)
            }
        }
        menuItem.isChecked = true
        return true
    }

    private fun handleLogout() {
        AlertDialog.Builder(this)
            .setTitle(MainConstants.TITLE_SIGN_OUT)
            .setMessage(MainConstants.MESSAGE_SIGN_OUT)
            .setNegativeButton(MainConstants.TXT_NEGATIVE_BUTTON){dialog, posicao -> }
            .setPositiveButton(MainConstants.TXT_POSITIVE_BUTTON) {dialog, posicao ->
                userPreferencesViewModel.clearUserPreferences()
                Toast.makeText(this, MainConstants.SUCCESS_SIGN_OUT_USER, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SigninActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .create()
            .show()
    }

    private fun applyThemeBasedOnMenuItem(itemId: Int, backgroundHeader: LinearLayout): String {
        return when (itemId) {
            R.id.nav_today -> applyTheme(R.color.todayColor, backgroundHeader, AppConstants.FILTER_TODAY)
            R.id.nav_tomorrow -> applyTheme(R.color.tomorrowColor, backgroundHeader, AppConstants.FILTER_TOMORROW)
            R.id.nav_week -> applyTheme(R.color.weekColor, backgroundHeader, AppConstants.FILTER_WEEK)
            R.id.nav_month -> applyTheme(R.color.monthColor, backgroundHeader, AppConstants.FILTER_MONTH)
            else -> applyTheme(R.color.todayColor, backgroundHeader, AppConstants.FILTER_TODAY) // Tema padrão
        }
    }

    private fun applyTheme(colorId: Int, backgroundHeader: LinearLayout, filter: String): String {
        backgroundHeader.setBackgroundResource(colorId)
        binding.fabAddTask.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, colorId))
        return filter
    }

    private fun openTasksFragment(filter: String) {
        val fragment = TasksFragment().apply {
            arguments = Bundle().apply {
                putString(AppConstants.KEY_PUT_EXTRA_FRAGMENT, filter)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            openCustomDialog()
        }
    }

    private fun openCustomDialog() {
        val viewDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val editText = viewDialog.findViewById<TextInputEditText>(R.id.editTxtTask)
        val calendarView = viewDialog.findViewById<CalendarView>(R.id.cvCompletionDate)


        val alertDialog = AlertDialog.Builder(this)
            .setView(viewDialog)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewDialog.findViewById<AppCompatButton>(R.id.btnSave).setOnClickListener {
            val inputText = editText.text.toString()
            if (inputText.isNotEmpty()) {
                Toast.makeText(this, inputText + " - " + calendarView.date, Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, AppConstants.EMPTY_INPUT_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.show()
    }
}