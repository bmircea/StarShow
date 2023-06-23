package com.example.starshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.starshow.data.ObservationData

class ObservationAdapter(var mList: List<Observation>) : RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder>() {

    inner class ObservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.logoIv)
        val title : TextView = itemView.findViewById(R.id.titleIv)
    }

    fun setFilteredList(mList: List<Observation>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.obs_card, parent, false)
        return ObservationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ObservationViewHolder, position: Int) {
        //holder.image.setImageBitmap(mList[position].image)
        holder.title.text = mList[position].name
    }
}