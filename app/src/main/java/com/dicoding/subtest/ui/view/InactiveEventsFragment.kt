package com.dicoding.subtest.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.subtest.adapter.EventAdapter
import com.dicoding.subtest.databinding.FragmentInactiveEventsBinding
import com.dicoding.subtest.ui.viewModel.EventViewModel

class InactiveEventsFragment : Fragment() {

    private var _binding: FragmentInactiveEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventAdapter
    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInactiveEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[EventViewModel::class.java]

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = EventAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        binding.progressBar.visibility = View.VISIBLE

        viewModel.inactiveEvents.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE
            if (events != null) {
                adapter.updateData(events)
            } else {
                Toast.makeText(requireContext(), "No inactive events found", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.fetchInactiveEvents()

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
        val filteredEvents = viewModel.inactiveEvents.value?.filter { event ->
            event.name.contains(query, ignoreCase = true) || event.summary.contains(
                query,
                ignoreCase = true
            )
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
