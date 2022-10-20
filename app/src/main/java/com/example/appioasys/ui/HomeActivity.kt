package com.example.appioasys.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appioasys.R
import com.example.appioasys.adapter.BusinessAdapter
import com.example.appioasys.data.response.CompanyListResponse
import com.example.appioasys.data.rest.RetrofitConfig
import com.example.appioasys.databinding.ActivityHomeBinding
import com.example.appioasys.domain.model.CompanyItemMapped
import com.example.appioasys.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val token: String
        get() = intent.getStringExtra(TOKEN).toString()
    private val client: String
        get() = intent.getStringExtra(CLIENT).toString()
    private val uid: String
        get() = intent.getStringExtra(UID).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.rouge)
        setupToolbar()
        recyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.homeToolbar)
        supportActionBar?.title = null
    }

    private fun recyclerView() {
        with(binding.homeRecyclerView) {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(
                    this@HomeActivity, LinearLayoutManager.VERTICAL,
                    false
                )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.menu_action_search)
        val searchView = searchItem.actionView as SearchView
        configureMenuOptions(searchView)

        searchView.queryHint = getString(R.string.home_field_search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() == true) {
                    requestCompanyData(newText)
                } else {
                    binding.homeRecyclerView.isVisible = false
                }
                return false
            }
        })
        return true
    }

    private fun configureMenuOptions(searchView: SearchView) {
        searchView.setOnSearchClickListener {
            with(binding) {
                homeUserIndicationTextView.isVisible = !homeUserIndicationTextView.isVisible
            }
            searchView.setOnCloseListener {
                with(binding) {
                    homeFieldResearchFailedTextView.isVisible = false
                    homeRecyclerView.isVisible = false
                    homeUserIndicationTextView.isVisible = !homeUserIndicationTextView.isVisible
                }
                false
            }
        }
    }

    private fun requestCompanyData(newText: String?) {
        val companyListService = RetrofitConfig.getRetrofit()
            .getEnterpriseList(token, client, uid, newText)
        val callList: Call<CompanyListResponse> = companyListService

        callList.enqueue(object : Callback<CompanyListResponse> {
            override fun onResponse(
                call: Call<CompanyListResponse>,
                response: Response<CompanyListResponse>
            ) {
                with(response) {
                    if (isSuccessful) {
                        handleBusinessDataResponse(
                            body()?.enterprises.toItem()
                        )
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

    private fun handleHomeDataFailure(throwable: Throwable, newText: String?) {
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

    private fun handleBusinessDataResponse(enterprises: List<CompanyItemMapped>?) {
        if (enterprises?.isNotEmpty() == true) {
            binding.homeRecyclerView.isVisible = true
            binding.homeRecyclerView.adapter = BusinessAdapter(enterprises) { companyItemResponse ->
                val intent = CompanyDetail.getStartIntent(
                    this@HomeActivity,
                    companyItemResponse.companyName,
                    companyItemResponse.photoUrl,
                    companyItemResponse.companyDescription
                )
                startActivity(intent)
            }
        } else {
            with(binding){
                homeRecyclerView.isVisible = false
                homeFieldResearchFailedTextView.isVisible = true
            }
        }
    }
}

