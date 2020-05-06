package com.threedee.nature.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.threedee.nature.R

class FarmDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_details)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, FarmDetailsActivity::class.java))
        }
    }
}
