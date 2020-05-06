package com.threedee.nature.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.threedee.domain.model.Farm
import com.threedee.nature.R
import com.threedee.nature.util.inflate
import kotlinx.android.synthetic.main.dashboard_info.view.*

class FarmersAdapter(
    private var items: ArrayList<Farm>,
    private val itemClick: (farm: Farm) -> Unit
) : RecyclerView.Adapter<FarmersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    fun setData(farms: List<Farm>) {
        items.clear()
        items.addAll(farms)
        notifyDataSetChanged()
    }

    inner class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.dashboard_info)) {
        fun bind(item: Farm) {
            //TODO: Add GlideApp and details and date format
            itemView.farmerNameTextView.text = "Christiana Olaoye"
            itemView.setOnClickListener { itemClick.invoke(item) }
        }
    }
}