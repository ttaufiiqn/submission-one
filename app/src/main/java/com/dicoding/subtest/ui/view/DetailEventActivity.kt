package com.dicoding.subtest.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.subtest.R
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.local.repository.FavoriteEventRepository
import com.dicoding.subtest.data.local.room.FavoriteEventDatabase
import com.dicoding.subtest.data.remote.response.EventDetail
import com.dicoding.subtest.databinding.ActivityDetailEventBinding
import com.dicoding.subtest.ui.viewModel.DetailEventViewModel
import com.dicoding.subtest.ui.viewModel.FavoriteViewModel
import com.dicoding.subtest.ui.viewModel.FavoriteViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var eventLink: String
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val detailEventViewModel: DetailEventViewModel by viewModels()

    private var isFavorite = false
    private lateinit var currentEvent: EventDetail

    companion object {
        private const val DELAY_MILLIS = 1500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FavoriteEventDatabase.getInstance(application)
        val favoriteEventDao = database.favoriteEventDao()
        val favoriteRepository = FavoriteEventRepository(favoriteEventDao)

        favoriteViewModel = ViewModelProvider(
            this,
            FavoriteViewModelFactory(favoriteRepository)
        )[FavoriteViewModel::class.java]

        val eventId = intent.getStringExtra("EVENT_ID")
        if (eventId != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                detailEventViewModel.fetchEventDetails(eventId)
            }, DELAY_MILLIS)
        }

        detailEventViewModel.event.observe(this) { eventResponse ->
            val event = eventResponse?.event
            if (event != null) {
                currentEvent = event
                bindEvent(event)

                // Check if the event is already a favorite
                favoriteViewModel.isFavorite(currentEvent.id.toString())
                    .observe(this) { favoriteEvent ->
                        isFavorite = favoriteEvent != null
                        binding.fabFavorite.setImageResource(
                            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                        )
                    }
            }
        }

        detailEventViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        detailEventViewModel.error.observe(this) { errorMessage ->
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
            if (!isFavorite) {
                val favoriteEvent = FavoriteEvent(
                    id = currentEvent.id.toString(),
                    name = currentEvent.name,
                    mediaCover = currentEvent.mediaCover
                )
                favoriteViewModel.insert(favoriteEvent)
                isFavorite = true
                fab.setImageResource(R.drawable.ic_favorite)
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
            } else {
                val favoriteEventToDelete = FavoriteEvent(
                    id = currentEvent.id.toString(),
                    name = currentEvent.name,
                    mediaCover = currentEvent.mediaCover
                )
                favoriteViewModel.delete(favoriteEventToDelete)
                isFavorite = false
                fab.setImageResource(R.drawable.ic_favorite_border)
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

            description.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
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
}
