package com.rgosdeveloper.tasks.presentation.activites

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rgosdeveloper.tasks.R
import com.rgosdeveloper.tasks.presentation.fragments.TasksFragment
import com.rgosdeveloper.tasks.databinding.ActivityMainBinding

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

        drawerLayout = binding.drawerLayout

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val navigationView = binding.navView

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        val headerView = navigationView.getHeaderView(0)
        val backgroundHeader = headerView.findViewById<LinearLayout>(R.id.backgroundHeader)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationDrawer(menuItem, backgroundHeader)
        }

        val defaultItem = navigationView.menu.findItem(R.id.nav_today)
        defaultItem.isChecked = true
        handleNavigationDrawer(defaultItem, backgroundHeader)

        binding.fabAddTask.setOnClickListener {
            actionFAB()
        }
    }

    private fun setWindowFlags() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun handleNavigationDrawer(menuItem: MenuItem, backgroundHeader: LinearLayout): Boolean {
        when (menuItem.itemId) {
            R.id.nav_logout -> {
                handleLogout()
                return true
            }
            else -> {
                val filter = applyThemeBasedOnMenu(menuItem.itemId, backgroundHeader)
                openTasksFragment(filter)
            }
        }
        menuItem.isCheckable = true
        return true
    }

    private fun handleLogout() {
        Toast.makeText(this, "Saindo do app", Toast.LENGTH_SHORT).show()
    }

    private fun applyThemeBasedOnMenu(itemId: Int, backgroundHeader: LinearLayout): String {

        return when (itemId) {
            R.id.nav_today -> {
                applyTheme(R.color.todayColor, backgroundHeader)
                "today"
            }
            R.id.nav_tomorrow -> {
                applyTheme(R.color.tomorrowColor, backgroundHeader)
                "tomorrow"
            }
            R.id.nav_week -> {
                applyTheme(R.color.weekColor, backgroundHeader)
                "week"
            }
            R.id.nav_month -> {
                applyTheme(R.color.monthColor, backgroundHeader)
                "month"
            }
            else -> {
                // Tema padrão
                applyTheme(R.color.todayColor, backgroundHeader)
                "today"
            }

        }
    }

    private fun applyTheme(colorId: Int, backgroundHeader: LinearLayout) {
        backgroundHeader.setBackgroundResource(colorId)
        val fab = binding.fabAddTask
        fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, colorId))
    }

    private fun openTasksFragment(filter: String) {
        val fragment = TasksFragment()
        val args = Bundle()
        args.putString("filter", filter)
        fragment.arguments = args

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun actionFAB() {
        val viewDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(viewDialog)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialog.show()
    }
}