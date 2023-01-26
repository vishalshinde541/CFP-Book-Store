package com.example.bookstore.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import com.example.bookstore.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavDrawerHandler {

    lateinit var toggle: ActionBarDrawerToggle
//    lateinit var drawerLayout: DrawerLayout
    private lateinit var fAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()

        supportFragmentManager.beginTransaction().replace(R.id.fragmentsContainer, HomeFragment())
            .commit()

//        val currentUser = fAuth.currentUser
//        if (currentUser != null) {
//            val addToBackStack = supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentsContainer, HomeFragment()).addToBackStack(null)
//                .commit()
//        } else {
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragmentsContainer, LoginFragment())
//                .commit()
//        }


        // Navigation Drawer
//        drawerLayout = findViewById(R.id.drawerLayout)
//        binding.navView
//        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Book Store")

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fun replaceFragment(fragment: Fragment, title: String) {

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentsContainer, fragment)
            fragmentTransaction.commit()
            binding.drawerLayout.closeDrawers()
            setTitle(title)
        }

        fun signOut() {

            val currentUser = fAuth.currentUser
            if (currentUser != null) {
                fAuth.signOut()
                replaceFragment(LoginFragment(), title.toString())
            } else {
                Toast.makeText(applicationContext, "YOU ARE NOTE LOGGED IN", Toast.LENGTH_SHORT).show()
            }


        }


        binding.navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {

                R.id.nav_home -> replaceFragment(HomeFragment(), it.title.toString())
                R.id.nav_setting -> Toast.makeText(applicationContext, "clicked on setting", Toast.LENGTH_SHORT).show()
                R.id.nav_wishlist -> Toast.makeText(applicationContext, "clicked Help and feedback", Toast.LENGTH_SHORT).show()
                R.id.nav_logOut -> signOut()
                R.id.nav_share -> Toast.makeText(applicationContext, "clicked Share", Toast.LENGTH_SHORT).show()
                R.id.nav_rateus -> Toast.makeText(applicationContext, "clicked Rate us", Toast.LENGTH_SHORT).show()
            }
            true
        }

    }

    private fun addFragment() {
        val fragmentManager = supportFragmentManager
        val dialogProfileFragment = DialogProfileFragment()
        dialogProfileFragment.show(fragmentManager, "dialogProfile")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opt_menu, menu)
        val menuItem: MenuItem? = menu?.findItem(R.id.opt_profile_Image)
        val view = MenuItemCompat.getActionView(menuItem)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        } else if (
            return when (item.itemId) {

                R.id.opt_profile_Image -> {

                    val currentUser = fAuth.currentUser
                    if (currentUser != null) {
                        addFragment()
                        Toast.makeText(this, "Clicked on profile", Toast.LENGTH_SHORT).show()
                        true
                    } else {
                        val fragmentManager = supportFragmentManager
                        val dialogProfileFragment = UserNotLogedInDialogFragment()
                        dialogProfileFragment.show(fragmentManager, "dialogProfile")
                        true
                    }


                }
                else -> return super.onOptionsItemSelected(item)
            }
        )
            toggle.syncState()
        return super.onOptionsItemSelected(item)
    }

    override fun setDrawerLocked() {
        super.setDrawerLocked()
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
}