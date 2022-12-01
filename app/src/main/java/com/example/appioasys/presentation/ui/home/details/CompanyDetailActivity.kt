package com.example.appioasys.presentation.ui.home.details

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
    private val companyItem by lazy { intent.getParcelableExtra(COMPANY_ITEM_EXTRA) as? CompanyItem? }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.rouge)
        setData()
        setupToolbar()
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.companyDetailsToolbar)
        title = companyItem?.name
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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