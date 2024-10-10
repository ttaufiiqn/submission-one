package com.dicoding.subtest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.subtest.data.response.EventResponse
import com.dicoding.subtest.data.response.ListEventsItem
import com.dicoding.subtest.data.retrofit.ApiConfig
import com.dicoding.subtest.databinding.FragmentEventListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventListFragment : Fragment() {

    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventAdapter
    private var allEvents: List<ListEventsItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.progressBar.visibility = View.VISIBLE

        fetchEvents()

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchEvents(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun fetchEvents() {
        ApiConfig.getApiService().getActiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val events = response.body()?.listEvents
                    if (events != null) {
                        allEvents = events
                        adapter = EventAdapter(events)
                        binding.recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(requireContext(), "Failed to load events", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load events", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed to load events", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchEvents(query: String) {
        val filteredEvents = allEvents?.filter { event ->
            event.name.contains(query, ignoreCase = true) || event.summary.contains(query, ignoreCase = true)
        }

        if (filteredEvents.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No events found", Toast.LENGTH_SHORT).show()
        } else {
            adapter = EventAdapter(filteredEvents)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
