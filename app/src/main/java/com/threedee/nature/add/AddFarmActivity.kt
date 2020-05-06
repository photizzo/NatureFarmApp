package com.threedee.nature.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.threedee.nature.R
import com.threedee.nature.databinding.ActivityAddFarmBinding
import com.threedee.nature.home.MainActivity
import dagger.android.support.DaggerAppCompatActivity

class AddFarmActivity : DaggerAppCompatActivity() {

    private lateinit var viewPagerAdapter: AddFarmPagerAdapter
    private lateinit var binding: ActivityAddFarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_farm)
        setupViewPager()
    }

    private fun setupViewPager() {
        viewPagerAdapter = AddFarmPagerAdapter(this)
        binding.viewpager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    selectedTabPosition = position
                }
            })
            currentItem = selectedTabPosition
//            isUserInputEnabled = false
        }
    }

    inner class AddFarmPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment = when (position) {
            ADD_FARMER_POSITION -> AddFarmersFragment()
            ADD_FARM_LOCATION_POSITION -> AddFarmLocationDetailsFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }

        override fun getItemCount(): Int = TRANSACTION_SCREENS_NUMBER
    }

    companion object {
        internal const val TRANSACTION_SCREEN_OFFSCREEN_LIMIT = 2
        internal const val TRANSACTION_SCREENS_NUMBER = 2

        internal const val ADD_FARMER_POSITION = 0
        internal const val ADD_FARM_LOCATION_POSITION = 1

        // Added here to not confuse with usages of this variable in onPageSelected()
        private var selectedTabPosition = 0

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AddFarmActivity::class.java))
        }
    }
}
