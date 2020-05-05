package com.threedee.nature.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.threedee.nature.login.LoginActivity
import com.threedee.nature.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Revert back to app theme before super.onCreate()
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        LoginActivity.startActivity(this)
        finish()
    }
}
