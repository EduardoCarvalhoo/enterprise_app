package com.example.appioasys.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appioasys.R
import com.example.appioasys.databinding.CompanyItemBinding
import com.example.appioasys.domain.model.CompanyItemMapped

class BusinessAdapter(
    private var enterprises: List<CompanyItemMapped>,
    private var onItemClickListener: (item: CompanyItemMapped) -> Unit
) : RecyclerView.Adapter<BusinessAdapter.ListBusinessViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBusinessViewHolder {
        val view = CompanyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListBusinessViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ListBusinessViewHolder, position: Int) {
        holder.bindView(enterprises[position])
    }

    override fun getItemCount() = enterprises.size

    class ListBusinessViewHolder(
        binding: CompanyItemBinding,
        private val onItemClickListener: (item: CompanyItemMapped) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val logo = binding.companyItemPhotoImageView
        private val name = binding.companyItemNameTextView
        private val kindOfService = binding.companyItemKindOfServiceTextView
        private val location = binding.companyItemLocationTextView

        fun bindView(item: CompanyItemMapped) {
            Glide.with(this@ListBusinessViewHolder.itemView).load(
                itemView.context.getString(
                    R.string.business_adapter_base_url_text,
                    item.photoUrl
                )
            )
                .into(logo)
            name.text = item.companyName
            kindOfService.text = item.kindOfService
            location.text = item.companyCity

            itemView.setOnClickListener {
                onItemClickListener.invoke(item)
            }
        }
    }
}
