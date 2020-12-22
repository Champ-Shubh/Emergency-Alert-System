package com.undispuated.alertsystem.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.undispuated.alertsystem.R

class SplashActivity : AppCompatActivity() {

    val SPLASH_SCREEN_DELAY: Long = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = Intent(this@SplashActivity, HomeActivity::class.java)

        Handler().postDelayed({
            startActivity(intent)
        }, SPLASH_SCREEN_DELAY)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
