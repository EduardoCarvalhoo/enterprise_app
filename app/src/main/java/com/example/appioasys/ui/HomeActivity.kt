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
import com.example.appioasys.databinding.ActivityHomeBinding
import com.example.appioasys.response.CompanyListResponse
import com.example.appioasys.response.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

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
        val token = intent.getStringExtra("access_token")
        val client = intent.getStringExtra("client")
        val uid = intent.getStringExtra("uid")
        requestCompanyData(token, client, uid, newText)
    }

    private fun requestCompanyData(
        token: String?,
        client: String?,
        uid: String?,
        typedText: String?
    ) {

        val companyListService = RetrofitConfig.getRetrofit()
            .getEnterpriseList(token.toString(), client.toString(), uid.toString(), typedText)
        val callList: Call<CompanyListResponse> = companyListService

        callList.enqueue(object : Callback<CompanyListResponse> {
            override fun onResponse(
                call: Call<CompanyListResponse>,
                response: Response<CompanyListResponse>
            ) {
                handleBusinessDataResponse(response)
            }

            override fun onFailure(call: Call<CompanyListResponse>, t: Throwable) {

            }
        })
    }

    private fun handleBusinessDataResponse(response: Response<CompanyListResponse>) {
        if (response.isSuccessful && (response.body()?.enterprises?.isNotEmpty() == true)) {
            binding.homeRecyclerView.isVisible = true
            binding.homeRecyclerView.adapter = BusinessAdapter(
                response.body()?.enterprises ?: return
            ) { companyItemResponse ->
                val intent = CompanyDetail.getStartIntent(
                    this@HomeActivity,
                    companyItemResponse.companyName,
                    companyItemResponse.photoUrl,
                    companyItemResponse.companyDescription
                )
                startActivity(intent)
            }
        }else{
            binding.homeRecyclerView.isVisible = false
            binding.homeFieldResearchFailedTextView.isVisible = true
        }
    }
}

