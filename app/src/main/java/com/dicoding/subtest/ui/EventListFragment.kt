package com.dicoding.subtest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.subtest.databinding.FragmentEventListBinding

class EventListFragment : Fragment() {

    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventAdapter
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = EventAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        binding.progressBar.visibility = View.VISIBLE

        viewModel.activeEvents.observe(viewLifecycleOwner, { events ->
            binding.progressBar.visibility = View.GONE
            if (events != null) {
                adapter.updateData(events)
            } else {
                Toast.makeText(requireContext(), "No active events found", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.fetchActiveEvents()

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

    private fun searchEvents(query: String) {
        val filteredEvents = viewModel.activeEvents.value?.filter { event ->
            event.name.contains(query, ignoreCase = true) || event.summary.contains(query, ignoreCase = true)
        }

        if (filteredEvents.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No events found", Toast.LENGTH_SHORT).show()
        } else {
            adapter.updateData(filteredEvents)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
