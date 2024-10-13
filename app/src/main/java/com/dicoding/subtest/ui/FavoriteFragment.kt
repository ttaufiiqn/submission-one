package com.dicoding.subtest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.repository.FavoriteRepository
import com.dicoding.subtest.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = FavoriteRepository(requireActivity().application)
        val viewModelFactory = FavoriteViewModelFactory(repository)

        favoriteViewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)

        favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteEvents ->
            val adapter = FavoriteAdapter(favoriteEvents)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}