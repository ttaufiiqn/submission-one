package com.dicoding.subtest.ui

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
import com.bumptech.glide.Glide
import com.dicoding.subtest.R
import com.dicoding.subtest.data.response.EventDetail
import com.dicoding.subtest.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var eventLink: String
    private val viewModel: DetailEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getStringExtra("EVENT_ID")
        if (eventId != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.fetchEventDetails(eventId)
            }, 3000)
        }

        viewModel.event.observe(this) { eventResponse ->
            val event = eventResponse?.event
            if (event != null) {
                bindEvent(event)
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
    }

    private fun bindEvent(event: EventDetail) {
        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.mediaCover)

        binding.name.text = event.name
        binding.summary.text = event.summary

        binding.description.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.description.movementMethod = LinkMovementMethod.getInstance()

        Glide.with(this)
            .load(event.imageLogo)
            .into(binding.logoImage)

        binding.category.text = getString(R.string.kategori, event.category)
        binding.ownerName.text = getString(R.string.owner, event.ownerName)
        binding.cityName.text = getString(R.string.kota, event.cityName)
        binding.quota.text = getString(R.string.quota, event.quota)
        binding.registrants.text = getString(R.string.registrants, event.registrants)
        binding.beginTime.text = getString(R.string.mulai, event.beginTime)
        binding.endTime.text = getString(R.string.berakhir, event.endTime)

        eventLink = event.link
    }
}
