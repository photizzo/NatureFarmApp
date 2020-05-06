package com.threedee.nature.add

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.threedee.nature.R
import com.threedee.nature.databinding.ActivityPickFarmCoordinatesBinding
import com.threedee.nature.home.MainActivity

class PickFarmCoordinatesActivity : AppCompatActivity() {

    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var binding: ActivityPickFarmCoordinatesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_farm_coordinates)
        initViews()
    }

    private fun initViews() {
        initBottomSheet()
        binding.bottomSheet.closeButton.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.bottomSheet.closeSheet.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun initBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheet)
        sheetBehavior.bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        }
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, PickFarmCoordinatesActivity::class.java))
        }
    }
}