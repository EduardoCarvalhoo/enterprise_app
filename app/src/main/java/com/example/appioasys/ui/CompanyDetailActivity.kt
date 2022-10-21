package com.example.appioasys.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.appioasys.R
import com.example.appioasys.databinding.ActivityCompanyDetailBinding
import com.example.appioasys.domain.model.CompanyItem
import com.example.appioasys.utils.BASE_IMAGE_URL

class CompanyDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCompanyDetailBinding.inflate(layoutInflater) }
    private val companyItem by lazy { intent.getSerializableExtra(COMPANY_ITEM_EXTRA) as CompanyItem? }

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
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            title = companyItem?.name.orEmpty()
        }
    }

    private fun setData() {
        with(binding) {
            companyDetailsDescriptionTextView.text = companyItem?.description.orEmpty()
            companyItem?.photoUrl?.let {
                Glide.with(this@CompanyDetailActivity).load(BASE_IMAGE_URL.plus(it))
                    .into(companyDetailsImageView)
            }
        }
    }

    companion object {
        private const val COMPANY_ITEM_EXTRA = "COMPANY_ITEM_EXTRA"

        fun getStartIntent(
            context: Context,
            companyItem: CompanyItem
        ): Intent {
            return Intent(context, CompanyDetailActivity::class.java).apply {
                putExtra(COMPANY_ITEM_EXTRA, companyItem)
            }
        }
    }
}