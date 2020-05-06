package com.threedee.nature.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.threedee.nature.R
import com.threedee.nature.databinding.LayoutFarmerDetailsBinding
import dagger.android.support.DaggerFragment

class AddFarmersFragment : DaggerFragment() {
    private lateinit var binding: LayoutFarmerDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.layout_farmer_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}