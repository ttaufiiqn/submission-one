package com.dicoding.subtest.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.subtest.R
import com.dicoding.subtest.data.response.ListEventsItem

class EventAdapter(
    private val events: List<ListEventsItem>
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageLogo: ImageView = itemView.findViewById(R.id.imageLogo)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val summary: TextView = itemView.findViewById(R.id.summary)
        private val cityName: TextView = itemView.findViewById(R.id.cityName)


        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailEventActivity::class.java)
                intent.putExtra("EVENT_ID", events[adapterPosition].id.toString())
                itemView.context.startActivity(intent)
            }
        }
        fun bind(event: ListEventsItem) {
            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(imageLogo)
            name.text = event.name
            summary.text = event.summary
            cityName.text = event.cityName
        }
    }
}