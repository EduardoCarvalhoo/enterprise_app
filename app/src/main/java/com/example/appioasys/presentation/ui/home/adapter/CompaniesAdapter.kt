package com.example.appioasys.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appioasys.databinding.ItemCompanyBinding
import com.example.appioasys.domain.model.CompanyItem
import com.example.appioasys.utils.BASE_IMAGE_URL

class CompaniesAdapter(
    private val enterprises: List<CompanyItem>,
    private val onItemClickListener: (item: CompanyItem) -> Unit
) : RecyclerView.Adapter<CompaniesAdapter.CompaniesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompaniesViewHolder {
        val view = ItemCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompaniesViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: CompaniesViewHolder, position: Int) {
        holder.bindView(enterprises[position])
    }

    override fun getItemCount() = enterprises.size

    inner class CompaniesViewHolder(
        private val binding: ItemCompanyBinding,
        private val onItemClickListener: (item: CompanyItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: CompanyItem) {
            with(binding) {
                Glide.with(this@CompaniesViewHolder.itemView).load(
                    BASE_IMAGE_URL.plus(item.photoUrl)
                ).into(itemCompanyPhotoImageView)
                itemCompanyNameTextView.text = item.name
                itemCompanyKindOfServiceTextView.text = item.serviceType
                itemCompanyLocationTextView.text = item.city

                itemView.setOnClickListener {
                    onItemClickListener.invoke(item)
                }
            }
        }
    }
}
