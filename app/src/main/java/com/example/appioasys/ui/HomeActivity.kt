package com.example.appioasys.ui

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
import com.example.appioasys.databinding.ActivityHomeBinding

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
                if (newText?.isEmpty() == true) {
                    binding.homeRecyclerView.isVisible = false
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
}

