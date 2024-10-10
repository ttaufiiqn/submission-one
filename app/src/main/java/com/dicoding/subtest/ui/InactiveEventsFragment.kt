package com.dicoding.subtest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.subtest.data.response.EventResponse
import com.dicoding.subtest.data.response.ListEventsItem // Pastikan Anda mengimpor tipe ini
import com.dicoding.subtest.data.retrofit.ApiConfig
import com.dicoding.subtest.databinding.FragmentInactiveEventsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InactiveEventsFragment : Fragment() {

    private var _binding: FragmentInactiveEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventAdapter
    private var allEvents: List<ListEventsItem>? = null // Untuk menyimpan semua event

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInactiveEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)

        binding.progressBar.visibility = View.VISIBLE

        // Fetch inactive events
        fetchInactiveEvents()

        // Setup search functionality
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

    private fun fetchInactiveEvents() {
        ApiConfig.getApiService().getInactiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val events = response.body()?.listEvents // Ini seharusnya List<ListEventsItem>
                    if (events != null) {
                        allEvents = events // Simpan semua event
                        adapter = EventAdapter(events)
                        binding.recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(context, "Failed to load events", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to load events", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Failed to load events", Toast.LENGTH_SHORT).show()
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
