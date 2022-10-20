package com.example.appioasys.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.appioasys.R
import com.example.appioasys.databinding.ActivityCompanyDetailBinding
import com.example.appioasys.utils.BASE_IMAGE_URL

class CompanyDetail : AppCompatActivity() {
    private val binding by lazy { ActivityCompanyDetailBinding.inflate(layoutInflater) }
    private val url by lazy { intent.getStringExtra(URL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.rouge)
        configureToolbar()
        setData()
    }

    private fun configureToolbar() {
        setSupportActionBar(binding.companyDetailsToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = null
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
    }

    private fun setData() {
        with(binding) {
            companyDetailsToolbar.title = intent.getStringExtra(NAME)
            companyDetailsDescriptionTextView.text = intent.getStringExtra(DESCRIPTION)
            Glide.with(this@CompanyDetail).load(BASE_IMAGE_URL.plus(url))
                .into(companyDetailsImageView)
        }
    }

    companion object {
        const val NAME = "name"
        const val URL = "photoURl"
        const val DESCRIPTION = "description"

        fun getStartIntent(
            context: Context,
            companyName: String?,
            photoUrl: String?,
            companyDescription: String?
        ): Intent {
            return Intent(context, CompanyDetail::class.java).apply {
                putExtra(NAME, companyName)
                putExtra(URL, photoUrl)
                putExtra(DESCRIPTION, companyDescription)
            }
        }
    }
}