package com.threedee.nature.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.threedee.nature.login.LoginActivity
import com.threedee.nature.R
import com.threedee.nature.add.PickFarmCoordinatesActivity
import com.threedee.nature.home.MainActivity
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Revert back to app theme before super.onCreate()
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        MainActivity.startActivity(this)
        finish()
    }
}
