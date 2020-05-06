package com.threedee.nature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.threedee.domain.model.Farm
import com.threedee.nature.databinding.DashboardInfoBinding

class FarmersAdapter(private var items: ArrayList<Farm>,
    private val itemClick: (farm: Farm) -> Unit) : RecyclerView.Adapter<FarmersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardInfoBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    fun setData(farms: List<Farm>){
        items.clear()
        items.addAll(farms)
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding: DashboardInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Farm) {
            with(binding) {
                //TODO: Add GlideApp and details and date format
                binding.farmerNameTextView.text = "Christiana Olaoye"
                binding.root.setOnClickListener { itemClick.invoke(item) }
            }
        }
    }
}