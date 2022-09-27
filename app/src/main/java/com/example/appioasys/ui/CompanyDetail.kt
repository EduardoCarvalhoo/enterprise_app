package com.example.appioasys.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.appioasys.R
import com.example.appioasys.databinding.ActivityCompanyDetailBinding

class CompanyDetail : AppCompatActivity() {
    private lateinit var binding: ActivityCompanyDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.rouge)
        configureBackButton()
        implementData()
    }

    private fun configureBackButton() {
        setSupportActionBar(binding.companyDetailsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }

    private fun implementData() {
        binding.companyDetailsToolbar.title = intent.getStringExtra("name")
        binding.companyDetailsDescriptionTextView.text = intent.getStringExtra("description")
        val url = intent.getStringExtra("photoUrl")
        val companyImage = binding.companyDetailsImageView
        Glide.with(this.applicationContext).load(
            applicationContext.getString(R.string.business_adapter_base_url_text, url)
        ).into(companyImage)
    }

    companion object {
        fun getStartIntent(
            context: Context,
            companyName: String?,
            photoUrl: String?,
            companyDescription: String?
        ): Intent {
            return Intent(context, CompanyDetail::class.java).apply {
                putExtra("name", companyName)
                putExtra("photoUrl", photoUrl)
                putExtra("description", companyDescription)
            }
        }
    }
}