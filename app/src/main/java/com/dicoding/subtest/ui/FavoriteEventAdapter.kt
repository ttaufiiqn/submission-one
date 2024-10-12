package com.dicoding.subtest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.databinding.ItemEventBinding

class FavoriteEventAdapter(private var events: List<FavoriteEvent>) :
    RecyclerView.Adapter<FavoriteEventAdapter.FavoriteEventViewHolder>() {

    class FavoriteEventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        val event = events[position]
        holder.binding.apply {
            textViewEventName.text = event.name
            Glide.with(imageLogo.context)
                .load(event.mediaCover)
                .into(imageLogo)
        }
    }

    override fun getItemCount() = events.size

    fun updateData(newEvents: List<FavoriteEvent>) {
        events = newEvents
        notifyDataSetChanged()
    }
}