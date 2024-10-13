package com.dicoding.subtest.ui.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.subtest.databinding.FragmentHomeBinding
import com.dicoding.subtest.Adapter.EventAdapter
import com.dicoding.subtest.ui.ViewModel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private lateinit var activeEventsAdapter: EventAdapter
    private lateinit var completedEventsAdapter: EventAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            activeEventsRecyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            activeEventsAdapter = EventAdapter(mutableListOf())
            activeEventsRecyclerView.adapter = activeEventsAdapter

            completedEventsRecyclerView.layoutManager = LinearLayoutManager(context)
            completedEventsAdapter = EventAdapter(mutableListOf())
            completedEventsRecyclerView.adapter = completedEventsAdapter
        }

        observeViewModel()
        viewModel.fetchActiveEvents()
        viewModel.fetchCompletedEvents()
    }

    private fun observeViewModel() {
        viewModel.activeEvents.observe(viewLifecycleOwner) { events ->
            activeEventsAdapter.updateData(events)
        }

        viewModel.completedEvents.observe(viewLifecycleOwner) { events ->
            completedEventsAdapter.updateData(events)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}