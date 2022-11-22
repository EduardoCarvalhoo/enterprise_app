package com.example.appioasys.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.appioasys.R
import com.example.appioasys.data.response.LoginAuthenticationUser
import com.example.appioasys.data.rest.api.CompanyListApiDataSource
import com.example.appioasys.databinding.ActivityHomeBinding
import com.example.appioasys.domain.model.CompanyItem
import com.example.appioasys.presentation.ui.home.adapter.CompaniesAdapter
import com.example.appioasys.presentation.ui.home.details.CompanyDetailActivity
import com.example.appioasys.utils.showAlertDialog

class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        HomeViewModel.HomeViewModelFactory(CompanyListApiDataSource()).create(HomeViewModel::class.java)
    }
    private val authenticationData by lazy {
        intent.getSerializableExtra(COMPANY_ITEM_EXTRA) as LoginAuthenticationUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.rouge)
        setupToolbar()
        setupObserver()
    }


    private fun setupToolbar() {
        setSupportActionBar(binding.homeToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.menu_action_search)
        val searchView = searchItem.actionView as SearchView
        setupSearchViewClickListeners(searchView)
        searchView.queryHint = getString(R.string.home_field_search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotBlank() == true) {
                    viewModel.getCompanyList(
                        authenticationData, newText
                    )
                } else {
                    binding.homeRecyclerView.isVisible = false
                }
                return false
            }
        })
        return true
    }

    private fun setupSearchViewClickListeners(searchView: SearchView) {
        with(binding) {
            searchView.setOnSearchClickListener {
                homeUserIndicationTextView.isVisible = false
            }
            searchView.setOnCloseListener {
                homeFieldResearchFailedTextView.isVisible = false
                homeUserIndicationTextView.isVisible = true
                false
            }
        }
    }

    private fun setupObserver() {
        viewModel.companyErrorLiveData.observe(this) {
            it?.let { liveErrorData ->
                showAlertDialog(liveErrorData)
            }
        }
        viewModel.companyListLiveData.observe(this) {
            it?.let { companyItems ->
                setupRecyclerView(companyItems)
            }
        }
        viewModel.companyLiveDataContent.observe(this) {
            with(binding) {
                if (it == false) {
                    homeRecyclerView.isVisible = false
                    homeFieldResearchFailedTextView.isVisible = true
                } else {
                    homeRecyclerView.isVisible = true
                    homeFieldResearchFailedTextView.isVisible = false
                }
            }
        }
    }

    private fun setupRecyclerView(companyItems: List<CompanyItem>) {
        binding.homeRecyclerView.adapter = CompaniesAdapter(companyItems) { item: CompanyItem ->
            val intent = CompanyDetailActivity.getStartIntent(
                this@HomeActivity, item
            )
            startActivity(intent)
        }
    }

    companion object {
        private const val COMPANY_ITEM_EXTRA = "COMPANY_ITEM_EXTRA"

        fun getStartIntent(
            context: Context,
            authenticationData: LoginAuthenticationUser,
        ): Intent {
            return Intent(context, HomeActivity::class.java).apply {
                putExtra(COMPANY_ITEM_EXTRA, authenticationData)
            }
        }
    }

}

