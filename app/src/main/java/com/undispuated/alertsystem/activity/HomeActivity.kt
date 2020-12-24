package com.undispuated.alertsystem.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.undispuated.alertsystem.R
import com.undispuated.alertsystem.fragment.FAQsFragment
import com.undispuated.alertsystem.fragment.HomeFragment
import com.undispuated.alertsystem.fragment.ProfileFragment

class HomeActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 7
    var mUsername = "Mr. Anonymous"
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var txtUserName: TextView
    lateinit var toolbar: Toolbar
    lateinit var sharedPreferences: SharedPreferences
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        sharedPreferences = getSharedPreferences("AlertSystem SharedPreference", Context.MODE_PRIVATE)

        title = getString(R.string.my_app_name)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)

        setUpToolbar()

        //Open the Home Fragment as the first screen user sees on entering the app
        openHomeScreen()

        var previousMenuItem: MenuItem? = null

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.syncState()

        mFirebaseAuth = FirebaseAuth.getInstance()

        mAuthStateListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser

            if (user != null){
                onSignInInitialize(user.displayName, user.uid)
            }
            else{
                onSignOutCleanUp()
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                            listOf(
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.EmailBuilder().build()
                            )
                        )
                        .build(),
                    RC_SIGN_IN
                )
            }
        }

        val headerView = navigationView.getHeaderView(0)
        txtUserName = headerView.findViewById(R.id.txtHeaderUsername)
        txtUserName.text = mUsername

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null)
                previousMenuItem?.isChecked = false

            it.isCheckable = true
            it.isChecked = true

            previousMenuItem = it

            when(it.itemId){

                R.id.menu_home -> {
                    openHomeScreen()
                    drawerLayout.closeDrawers()
                }

                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                        ProfileFragment()
                        )
                        .commit()

                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.menu_faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FAQsFragment()
                        )
                        .commit()

                    supportActionBar?.title = "FAQ Section"
                    drawerLayout.closeDrawers()
                }

                R.id.menu_logout -> {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Logout Confirmation")
                    dialog.setMessage("Are you sure you want to logout?")

                    dialog.setPositiveButton("Yes"){_, _->
                        //Clear sharedPreferences and sign out from Firebase
                        AuthUI.getInstance().signOut(this)
                    }

                    dialog.setNegativeButton("No"){_, _ ->
                        //Remain there
                        drawerLayout.closeDrawers()
                    }

                    dialog.create()
                    dialog.show()
                }
            }

            return@setNavigationItemSelectedListener true
        }
    }

    //Helper method to open Home Screen
    private fun openHomeScreen(){
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()

        supportActionBar?.title = getString(R.string.my_app_name)
        navigationView.setCheckedItem(R.id.menu_home)
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.my_app_name)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this,"Sign in successful",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Signed Out !!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun onSignOutCleanUp() {
        mUsername = " Mr. Anonymous"
    }

    private fun onSignInInitialize(displayName: String?, uid: String) {
        mUsername = displayName.toString()
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
        sharedPreferences.edit().putString("username",mUsername).apply()
        sharedPreferences.edit().putString("id",uid).apply()
        sharedPreferences.edit().putInt("contacts_count", 0).apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_sign_out -> {
                AuthUI.getInstance().signOut(this)
                return true
            }

            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frameLayout)

        when(fragment){
            !is HomeFragment -> openHomeScreen()

            else -> ActivityCompat.finishAffinity(this)
        }
    }
}