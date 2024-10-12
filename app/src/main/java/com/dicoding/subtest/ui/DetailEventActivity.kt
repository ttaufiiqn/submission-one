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
import com.dicoding.subtest.data.remote.response.EventDetail
import com.dicoding.subtest.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var eventLink: String
    private val viewModel: DetailEventViewModel by viewModels()

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
}