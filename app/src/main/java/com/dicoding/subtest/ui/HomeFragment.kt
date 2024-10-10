package com.dicoding.subtest.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.subtest.data.response.EventResponse
import com.dicoding.subtest.data.response.ListEventsItem
import com.dicoding.subtest.data.retrofit.ApiConfig
import com.dicoding.subtest.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var activeEventsAdapter: EventAdapter
    private lateinit var completedEventsAdapter: EventAdapter
    private val activeEventsList = mutableListOf<ListEventsItem>()
    private val completedEventsList = mutableListOf<ListEventsItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activeEventsRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        activeEventsAdapter = EventAdapter(activeEventsList)
        binding.activeEventsRecyclerView.adapter = activeEventsAdapter

        binding.completedEventsRecyclerView.layoutManager = LinearLayoutManager(context)
        completedEventsAdapter = EventAdapter(completedEventsList)
        binding.completedEventsRecyclerView.adapter = completedEventsAdapter

        fetchActiveEvents()
        fetchCompletedEvents()
    }

    private fun fetchActiveEvents() {
        binding.progressBar.visibility = View.VISIBLE

        ApiConfig.getApiService().getActiveEvents().enqueue(object : Callback<EventResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val events = response.body()?.listEvents?.take(5)
                    if (events != null) {
                        activeEventsList.clear()
                        activeEventsList.addAll(events)
                        activeEventsAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "No active events found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to load active events", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Failed to load active events", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCompletedEvents() {
        binding.progressBar.visibility = View.VISIBLE

        ApiConfig.getApiService().getInactiveEvents().enqueue(object : Callback<EventResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val events = response.body()?.listEvents?.take(5)
                    if (events != null) {
                        completedEventsList.clear()
                        completedEventsList.addAll(events)
                        completedEventsAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "No completed events found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to load completed events", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Failed to load completed events", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
