package com.dicoding.subtest.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.subtest.data.remote.response.ListEventsItem
import com.dicoding.subtest.databinding.ItemEventBinding
import com.dicoding.subtest.ui.View.DetailEventActivity

class EventAdapter(private var events: List<ListEventsItem>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.textViewEventName.text = event.name
            binding.textViewSummary.text = event.summary
            binding.textViewCityName.text = event.cityName

            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(binding.imageLogo)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailEventActivity::class.java)
                intent.putExtra("EVENT_ID", event.id.toString())
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = events.size

    fun updateData(newEvents: List<ListEventsItem>) {
        val diffCallback = EventDiffCallback(events, newEvents)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        events = newEvents
        diffResult.dispatchUpdatesTo(this)
    }
}

class EventDiffCallback(
    private val oldList: List<ListEventsItem>,
    private val newList: List<ListEventsItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}