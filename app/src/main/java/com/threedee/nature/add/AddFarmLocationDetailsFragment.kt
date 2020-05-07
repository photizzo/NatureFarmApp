package com.threedee.nature.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.threedee.nature.R
import com.threedee.nature.databinding.LayoutFarmDetailsBinding
import com.threedee.presentation.viewmodel.FarmViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AddFarmLocationDetailsFragment : DaggerFragment() {
    private lateinit var binding: LayoutFarmDetailsBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var farmViewModel: FarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.layout_farm_details, container, false)
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.pickCoordinatorButton.setOnClickListener {
            PickFarmCoordinatesActivity.startActivity(view.context)
        }
        binding.previewButton.setOnClickListener {
            farmViewModel.currentPage.value = 0
        }
    }

    private fun initViewModel() {
        activity?.let {
            farmViewModel = ViewModelProvider(it, viewModelFactory).get(FarmViewModel::class.java)
        }
    }
}