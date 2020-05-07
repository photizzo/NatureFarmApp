package com.threedee.nature.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.threedee.nature.R
import com.threedee.nature.databinding.ActivitySuccessBinding

class SuccessActivity : AppCompatActivity() {

    lateinit var binding: ActivitySuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_success)

        binding.continueButton.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(
                Intent(context, SuccessActivity::class.java)
            )
        }
    }
}
