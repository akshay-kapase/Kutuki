package com.example.kutuki.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.kutuki.databinding.HomeActBinding
import com.example.kutuki.utils.NetworkResult
import com.example.kutuki.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.GridLayoutManager

@AndroidEntryPoint
class HomeAct : BaseAct() {

    lateinit var binding: HomeActBinding
    lateinit var categoryAdapter: CategoryAdapter
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListView()
        fetchData()
    }

    private fun setListView() {
        val onCategoryClicked: (category: String) -> Unit = { category ->
            val intent = Intent(this@HomeAct, PlayerAct::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
        }
        categoryAdapter = CategoryAdapter(onCategoryClicked)
        binding.rvCategories.apply {
            // Displaying data in a Grid design
            layoutManager = GridLayoutManager(this@HomeAct, 2, GridLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    private fun fetchData() {
        fetchResponse()
        homeViewModel.response.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        categoryAdapter.updateData(it.response.videoCategories)
                        Log.d("HomeActCategory %s",it.response.videoCategories.toString())
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                is NetworkResult.Error -> {
                    binding.pbLoader.visibility = View.GONE
                    Toast.makeText(
                        this,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun fetchResponse() {
        homeViewModel.fetchCategories()
        binding.pbLoader.visibility = View.VISIBLE
    }

}