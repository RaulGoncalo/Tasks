package com.rgosdeveloper.tasks.presentation.activites

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.presentation.fragments.TasksFragment
import com.rgosdeveloper.tasks.databinding.ActivityMainBinding
import com.rgosdeveloper.tasks.utils.AppConstants

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        setWindowFlags()
        setupDrawerLayout()
        setupNavigationView()
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
        Toast.makeText(this, AppConstants.LOGOUT_MESSAGE, Toast.LENGTH_SHORT).show()
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
                putString("filter", filter)
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