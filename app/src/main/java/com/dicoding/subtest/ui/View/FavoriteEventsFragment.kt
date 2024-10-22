package com.dicoding.subtest.ui.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.subtest.R
import com.dicoding.subtest.ui.adapter.FavoriteEventAdapter
import com.dicoding.subtest.ui.viewmodel.FavoriteViewModel

class FavoriteEventsFragment : Fragment() {

    private lateinit var favoriteEventAdapter: FavoriteEventAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_events, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_favorite_events)
        favoriteEventAdapter = FavoriteEventAdapter()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = favoriteEventAdapter

        viewModel.getAllFavoriteEvents().observe(viewLifecycleOwner, Observer { favoriteEvents ->
            favoriteEventAdapter.submitList(favoriteEvents)
        })

        return view
    }
}
