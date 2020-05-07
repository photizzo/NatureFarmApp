package com.threedee.nature.add

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.threedee.domain.model.Farm
import com.threedee.nature.R
import com.threedee.nature.databinding.ActivityAddFarmBinding
import com.threedee.nature.eventBus.MessageEvent
import com.threedee.nature.eventBus.RxBus
import com.threedee.nature.home.FarmDetailsActivity
import com.threedee.nature.home.MainActivity
import com.threedee.presentation.viewmodel.FarmViewModel
import dagger.android.support.DaggerAppCompatActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

class AddFarmActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var farmViewModel: FarmViewModel
    private lateinit var viewPagerAdapter: AddFarmPagerAdapter
    private lateinit var binding: ActivityAddFarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_farm)
        initViewModel()
        setupViewPager()
    }

    private fun initViewModel() {
        farmViewModel = ViewModelProvider(this, viewModelFactory).get(FarmViewModel::class.java)
        farmViewModel.currentPage.observe(this, Observer { currentPage ->
            Timber.e("current page here: $currentPage")
           binding.viewpager.currentItem = currentPage
        })
        farmViewModel.addFarmLiveData.observe(this, Observer {
            finish()
        })
        if (intent.hasExtra("data")){
            val farm = getFarmFromString(intent.getStringExtra("data") ?: return)
            farmViewModel.farmer.value = farm.farmer
            farmViewModel.farmLocation.value = farm.farmLocation
        }
    }

    private fun setupViewPager() {
        viewPagerAdapter = AddFarmPagerAdapter(this)
        binding.viewpager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            currentItem = farmViewModel.currentPage.value ?: 0
            isUserInputEnabled = false
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

    fun getFarmFromString(data: String): Farm {
        val listType = object : TypeToken<Farm>(){}.type
        return Gson().fromJson(data, listType)
    }

    companion object {
        internal const val TRANSACTION_SCREENS_NUMBER = 2
        internal const val ADD_FARMER_POSITION = 0
        internal const val ADD_FARM_LOCATION_POSITION = 1

        fun startActivity(context: Context, data: String?) {
            context.startActivity(Intent(context, AddFarmActivity::class.java)
                .apply {
                    if (data != null){
                        putExtra("data", data)
                    }
                })
        }
    }
}
