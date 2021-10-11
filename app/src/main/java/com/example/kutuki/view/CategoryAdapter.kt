package com.example.kutuki.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kutuki.R
import com.example.kutuki.databinding.RvHolderBinding
import com.example.kutuki.model.CategoryModel
import timber.log.Timber
import kotlin.properties.Delegates

class CategoryAdapter(val onCategoryClicked: (category: String) -> Unit) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>(){

    lateinit var binding: RvHolderBinding

    private var categories: List<CategoryModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    inner class CategoryHolder(val binding: RvHolderBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        override fun onClick(itemView: View?) {
            Log.d("CategoryClicked ${categories[adapterPosition].name}","")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryHolder {
        binding = RvHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Timber.d("HITHERE")
        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryHolder, position: Int) {
        with(holder) {
            with(categories[position]) {
                with(binding) {
                    ivCategory.load(categories[holder.adapterPosition].image) {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_background)
                        transformations(CircleCropTransformation())
                    }
                    holder.itemView.setOnClickListener{
                        Timber.d("holderViewClicked")
                        onCategoryClicked.invoke(categories[position].name)
                    }
                }
            }
        }
    }

    private fun onViewClicked(position: Int) {
        Timber.d("CategoryClicked ${categories[position].name}")
        onCategoryClicked.invoke(categories[position].name)
    }

    fun updateData(mCategories: MutableList<CategoryModel>) {
        categories = mCategories
        Timber.d("HITHEREFROMUPDATEDATA","")
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

}