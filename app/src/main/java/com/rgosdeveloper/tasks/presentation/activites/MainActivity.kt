package com.rgosdeveloper.tasks.presentation.activites

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

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

        navigationView.setNavigationItemSelectedListener { menuItem ->
            val filter = when (menuItem.itemId) {
                R.id.nav_today -> "today"
                R.id.nav_tomorrow -> "tomorrow"
                R.id.nav_week -> "week"
                R.id.nav_month -> "month"
                R.id.nav_logout -> {
                    Toast.makeText(this, "Saindo do app", Toast.LENGTH_SHORT).show()
                    "today"
                }
                else -> "today"
            }
            openTasksFragment(filter)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        val defaultItem = navigationView.menu.findItem(R.id.nav_today)
        defaultItem.isChecked = true
        openTasksFragment("today")
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
}