package com.dicoding.subtest.ui.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.dicoding.subtest.R
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.local.repository.FavoriteEventRepository
import com.dicoding.subtest.data.local.room.FavoriteEventDatabase
import com.dicoding.subtest.data.remote.response.EventDetail
import com.dicoding.subtest.databinding.ActivityDetailEventBinding
import com.dicoding.subtest.ui.ViewModel.DetailEventViewModel
import com.dicoding.subtest.ui.ViewModel.FavoriteViewModel
import com.dicoding.subtest.ui.ViewModel.FavoriteViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var eventLink: String
    private val viewModel: DetailEventViewModel by viewModels()

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        val database = FavoriteEventDatabase.getInstance(application)
        val favoriteEventDao = database.favoriteEventDao()
        val favoriteRepository = FavoriteEventRepository(favoriteEventDao)
        FavoriteViewModelFactory(favoriteRepository)
    }

    private var isFavorite = false  // Flag to track favorite status
    private lateinit var currentEvent: EventDetail

    companion object {
        private const val DELAY_MILLIS = 1500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getStringExtra("EVENT_ID")
        if (eventId != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.fetchEventDetails(eventId)
            }, DELAY_MILLIS)
        }

        viewModel.event.observe(this) { eventResponse ->
            val event = eventResponse?.event
            if (event != null) {
                currentEvent = event // Store the current event
                bindEvent(event)

                // Check if the event is already a favorite
                favoriteViewModel.isFavorite(currentEvent.id.toString()).observe(this) { favoriteEvent ->
                    isFavorite = favoriteEvent != null // Set isFavorite to true if favoriteEvent is not null
                    binding.fabFavorite.setImageResource(
                        if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                    )
                }
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.openLinkButton.setOnClickListener {
            if (::eventLink.isInitialized) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventLink))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Link is not available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fabFavorite.setOnClickListener {
            val fab = it as FloatingActionButton
            Log.d("FAB Click", "Current isFavorite state: $isFavorite")
            if (!isFavorite) {
                // Add to favorites
                val favoriteEvent = FavoriteEvent(
                    id = currentEvent.id.toString(),
                    name = currentEvent.name,
                    mediaCover = currentEvent.mediaCover
                )
                favoriteViewModel.insert(favoriteEvent)  // Ensure this is a suspend function
                isFavorite = true
                fab.setImageResource(R.drawable.ic_favorite) // Change icon to favorite
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                // Optionally navigate to favorites here if needed
                // navigateToFavorites()
            } else {
                // Remove from favorites
                val favoriteEventToDelete = FavoriteEvent(
                    id = currentEvent.id.toString(),
                    name = currentEvent.name,
                    mediaCover = currentEvent.mediaCover
                )
                favoriteViewModel.delete(favoriteEventToDelete) // Pass the FavoriteEvent object
                isFavorite = false
                fab.setImageResource(R.drawable.ic_favorite_border) // Change icon back
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindEvent(event: EventDetail) {
        with(binding) {
            Glide.with(this@DetailEventActivity)
                .load(event.mediaCover)
                .into(mediaCover)

            name.text = event.name
            summary.text = event.summary

            description.text =
                HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            description.movementMethod = LinkMovementMethod.getInstance()

            Glide.with(this@DetailEventActivity)
                .load(event.imageLogo)
                .into(logoImage)

            category.text = getString(R.string.kategori, event.category)
            ownerName.text = getString(R.string.owner, event.ownerName)
            cityName.text = getString(R.string.kota, event.cityName)
            quota.text = getString(R.string.quota, event.quota)
            registrants.text = getString(R.string.registrants, event.registrants)
            beginTime.text = getString(R.string.mulai, event.beginTime)
            endTime.text = getString(R.string.berakhir, event.endTime)

            val remainingQuotaValue = event.quota - event.registrants
            remainingQuota.text = getString(R.string.remaining_quota, remainingQuotaValue)
            eventLink = event.link
        }
    }

    private fun navigateToFavorites() {
        val navController = findNavController(R.id.nav_host_fragment)
        if (navController.currentDestination?.id != R.id.favoriteEventsFragment) {
            navController.navigate(R.id.favoriteEventsFragment)
        }
    }
}
