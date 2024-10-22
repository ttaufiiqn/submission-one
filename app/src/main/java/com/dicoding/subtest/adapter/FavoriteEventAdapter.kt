package com.dicoding.subtest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.subtest.R
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.databinding.ItemFavoriteEventBinding

class FavoriteEventAdapter :
    ListAdapter<FavoriteEvent, FavoriteEventAdapter.FavoriteEventViewHolder>(
        FavoriteEventDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding =
            ItemFavoriteEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        val favoriteEvent = getItem(position)
        holder.bind(favoriteEvent)
    }

    inner class FavoriteEventViewHolder(private val binding: ItemFavoriteEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteEvent: FavoriteEvent) {
            // Set the event name
            binding.eventName.text = favoriteEvent.name

            // Load the event image using Glide
            Glide.with(binding.root.context)
                .load(favoriteEvent.mediaCover) // Use the mediaCover URL from FavoriteEvent
                .into(binding.eventImage) // Ensure this matches the ID in your layout XML
        }
    }

    class FavoriteEventDiffCallback : DiffUtil.ItemCallback<FavoriteEvent>() {
        override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
            return oldItem == newItem
        }
    }
}
