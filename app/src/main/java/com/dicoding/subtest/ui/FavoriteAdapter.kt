package com.dicoding.subtest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val favoriteEvents: List<FavoriteEvent>) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEvent: FavoriteEvent) {
            binding.textViewEventName.text = favoriteEvent.name
            Glide.with(itemView.context)
                .load(favoriteEvent.mediaCover)
                .into(binding.imageViewEvent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteEvent = favoriteEvents[position]
        holder.bind(favoriteEvent)
    }

    override fun getItemCount(): Int = favoriteEvents.size
}