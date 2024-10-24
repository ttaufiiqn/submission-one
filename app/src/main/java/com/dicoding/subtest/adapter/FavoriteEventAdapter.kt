package com.dicoding.subtest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.databinding.ItemFavoriteEventBinding
import com.dicoding.subtest.ui.view.DetailEventActivity

class FavoriteEventAdapter :
    ListAdapter<FavoriteEvent, FavoriteEventAdapter.FavoriteEventViewHolder>(
        FavoriteEventDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemFavoriteEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        val favoriteEvent = getItem(position)
        holder.bind(favoriteEvent)
    }

    inner class FavoriteEventViewHolder(private val binding: ItemFavoriteEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteEvent: FavoriteEvent) {
            binding.eventName.text = favoriteEvent.name

            Glide.with(binding.root.context)
                .load(favoriteEvent.mediaCover)
                .into(binding.eventImage)

            // Set the click listener to navigate to the detail page
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailEventActivity::class.java)
                intent.putExtra("EVENT_ID", favoriteEvent.id)  // Pass the event ID
                itemView.context.startActivity(intent)
            }
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
