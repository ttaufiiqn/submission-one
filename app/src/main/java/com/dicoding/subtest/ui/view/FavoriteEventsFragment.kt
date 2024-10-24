package com.dicoding.subtest.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.subtest.R
import com.dicoding.subtest.adapter.FavoriteEventAdapter
import com.dicoding.subtest.data.local.repository.FavoriteEventRepository
import com.dicoding.subtest.data.local.room.FavoriteEventDatabase
import com.dicoding.subtest.ui.viewModel.FavoriteViewModel
import com.dicoding.subtest.ui.viewModel.FavoriteViewModelFactory

class FavoriteEventsFragment : Fragment() {

    private lateinit var favoriteEventAdapter: FavoriteEventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_events, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_favorite_events)
        favoriteEventAdapter = FavoriteEventAdapter()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = favoriteEventAdapter

        val database = FavoriteEventDatabase.getInstance(requireContext())
        val repository = FavoriteEventRepository(database.favoriteEventDao())
        favoriteViewModel =
            FavoriteViewModelFactory(repository).create(FavoriteViewModel::class.java)

        favoriteViewModel.getAllFavoriteEvents().observe(viewLifecycleOwner) { favoriteEvents ->
            favoriteEventAdapter.submitList(favoriteEvents)
        }

        return view
    }
}