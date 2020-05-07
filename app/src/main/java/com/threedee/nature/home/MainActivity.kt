package com.threedee.nature.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.threedee.domain.model.Farm
import com.threedee.nature.R
import com.threedee.nature.add.AddFarmActivity
import com.threedee.nature.databinding.ActivityLoginBinding
import com.threedee.nature.databinding.ActivityMainBinding
import com.threedee.nature.util.isToday
import com.threedee.nature.util.showSnackbar
import com.threedee.presentation.state.Resource
import com.threedee.presentation.state.ResourceState
import com.threedee.presentation.viewmodel.FarmViewModel
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var farmViewModel: FarmViewModel
    private var farmersAdapter = FarmersAdapter(arrayListOf()) { farm ->
        itemClick(farm)
    }
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initViewModel()
        initViews()
    }

    private fun itemClick(item: Farm) {
        FarmDetailsActivity.startActivity(this)
    }

    private fun initViewModel() {
        farmViewModel = ViewModelProvider(this, viewModelFactory).get(FarmViewModel::class.java)
        farmViewModel.getFarmsLiveData.observe(this, Observer { resource ->
            handleGetFarms(resource)
        })
    }

    override fun onResume() {
        super.onResume()
        farmViewModel.getFarms()
    }

    private fun initViews() {
        binding.farmerRecyclerView.adapter = farmersAdapter
        binding.farmerRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.farmerRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.floatingActionButton.setOnClickListener {
            AddFarmActivity.startActivity(this)
        }
    }

    private fun setDashboardData(farms: List<Farm>) {
        binding.totalFarmers.text = farms.size.toString()
        binding.statFarmers.text = farms.filter { farm ->
            farm.farmer.timeStamp.isToday(this)
        }.size.toString()
    }

    private fun handleGetFarms(resource: Resource<List<Farm>>) {
        when (resource.status) {
            ResourceState.LOADING -> {
            }
            ResourceState.SUCCESS -> {
                resource.data?.let { farms ->
                    if (farms.isEmpty()) {
                        binding.emptyTextView.visibility = View.VISIBLE
                        binding.farmerRecyclerView.visibility = View.GONE
                    } else {
                        binding.emptyTextView.visibility = View.GONE
                        binding.farmerRecyclerView.visibility = View.VISIBLE
                        setDashboardData(farms)
                        farmersAdapter.setData(farms)
                    }
                }
            }
            ResourceState.ERROR -> {
                resource.message?.let {
                    Timber.e("message: $it")
                    showSnackbar(it) }
            }
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
