package com.example.appioasys.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
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
    private lateinit var binding: ActivityHomeBinding
    private lateinit var token: String
    private lateinit var client: String
    private lateinit var uid: String
    private lateinit var typedText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
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
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.menu_action_search)
        val searchView = searchItem.actionView as SearchView
        configureMenuOptions(searchView)

        searchView.queryHint = getString(R.string.home_field_search_text)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() == true) {
                    receiveAuthenticationData(newText)
                } else {
                    with(binding) {
                        homeRecyclerView.isVisible = false
                    }
                }
                return false
            }
        })
        return true
    }

    private fun configureMenuOptions(searchView: SearchView) {
        if (searchView.isNotEmpty()) {
            searchView.setOnSearchClickListener {
                with(binding) {
                    homeUserIndicationTextView.isVisible = !homeUserIndicationTextView.isVisible
                }
            }
            searchView.setOnCloseListener(object : View.OnClickListener,
                SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    with(binding) {
                        binding.homeFieldResearchFailedTextView.isVisible = false
                        binding.homeRecyclerView.isVisible = false
                        homeUserIndicationTextView.isVisible = !homeUserIndicationTextView.isVisible
                    }
                    return false
                }

                override fun onClick(v: View?) {}
            })
        }
    }

    private fun receiveAuthenticationData(newText: String) {
        val intent: Intent = intent
        typedText = newText
        token = intent.getStringExtra(TOKEN).toString()
        client = intent.getStringExtra(CLIENT).toString()
        uid = intent.getStringExtra(UID).toString()
        requestCompanyData(token, client, uid, newText)
    }

    private fun requestCompanyData(
        token: String?, client: String?, uid: String?, typedText: String?
    ) {
        val companyListService = RetrofitConfig.getRetrofit()
            .getEnterpriseList(token.toString(), client.toString(), uid.toString(), typedText)
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
                            requestCompanyData(
                                token,
                                client,
                                uid,
                                typedText
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CompanyListResponse>, t: Throwable) {
                handleHomeDataFailure(t)
            }
        })
    }

    private fun handleHomeDataFailure(throwable: Throwable) {
        if (throwable is IOException) {
            showAlertDialog(getString(R.string.no_internet_connection_error_text)) {
                requestCompanyData(token, client, uid, typedText)
            }
        } else {
            showAlertDialog(getString(R.string.generic_error_text)) {
                requestCompanyData(token, client, uid, typedText)
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
            binding.homeRecyclerView.isVisible = false
            binding.homeFieldResearchFailedTextView.isVisible = true
        }
    }
}

