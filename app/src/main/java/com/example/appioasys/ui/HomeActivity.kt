package com.example.appioasys.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.appioasys.R
import com.example.appioasys.data.response.CompanyListResponse
import com.example.appioasys.data.rest.RetrofitConfig
import com.example.appioasys.databinding.ActivityHomeBinding
import com.example.appioasys.domain.model.CompanyItem
import com.example.appioasys.domain.model.toItem
import com.example.appioasys.ui.adapter.CompaniesAdapter
import com.example.appioasys.utils.CLIENT
import com.example.appioasys.utils.TOKEN
import com.example.appioasys.utils.UID
import com.example.appioasys.utils.showAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val token by lazy { intent.getStringExtra(TOKEN) }
    private val client by lazy { intent.getStringExtra(CLIENT) }
    private val uid by lazy { intent.getStringExtra(UID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (!validateScreenArgs()) return
        window.statusBarColor = ContextCompat.getColor(this, R.color.rouge)
        setupToolbar()
    }

    private fun validateScreenArgs() =
        if (token.isNullOrBlank() || client.isNullOrBlank() || uid.isNullOrBlank()) {
            showAlertDialog(getString(R.string.generic_error_text))
            false
        } else {
            true
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
                    requestCompanyData(newText)
                } else {
                    binding.homeRecyclerView.isVisible = false
                }
                return false
            }
        })
        return true
    }

    private fun setupSearchViewClickListeners(searchView: SearchView) {
        searchView.setOnSearchClickListener {
            binding.homeUserIndicationTextView.isVisible = false
        }
        searchView.setOnCloseListener {
            with(binding) {
                homeFieldResearchFailedTextView.isVisible = false
                homeUserIndicationTextView.isVisible = true
            }
            false
        }
    }

    private fun requestCompanyData(newText: String) {
        val companyListService = RetrofitConfig.getRetrofit()
            .getEnterpriseList(token.orEmpty(), client.orEmpty(), uid.orEmpty(), newText)
        val callList: Call<CompanyListResponse> = companyListService

        callList.enqueue(object : Callback<CompanyListResponse> {
            override fun onResponse(
                call: Call<CompanyListResponse>,
                response: Response<CompanyListResponse>
            ) {
                with(response) {
                    if (isSuccessful) {
                        handleBusinessDataResponse(body()?.companies.toItem())
                    } else {
                        showAlertDialog(getString(R.string.server_error_text)) {
                            requestCompanyData(newText)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CompanyListResponse>, t: Throwable) {
                handleHomeDataFailure(t, newText)
            }
        })
    }

    private fun handleHomeDataFailure(throwable: Throwable, newText: String) {
        if (throwable is IOException) {
            showAlertDialog(getString(R.string.no_internet_connection_error_text)) {
                requestCompanyData(newText)
            }
        } else {
            showAlertDialog(getString(R.string.generic_error_text)) {
                requestCompanyData(newText)
            }
        }
    }

    private fun handleBusinessDataResponse(enterprises: List<CompanyItem>?) {
        with(binding) {
            if (enterprises?.isNotEmpty() == true) {
                homeRecyclerView.isVisible = true
                homeRecyclerView.adapter = CompaniesAdapter(enterprises) { companyItem ->
                    val intent = CompanyDetailActivity.getStartIntent(
                        this@HomeActivity,
                        companyItem
                    )
                    startActivity(intent)
                }
            } else {
                homeRecyclerView.isVisible = false
                homeFieldResearchFailedTextView.isVisible = true
            }
        }
    }
}

