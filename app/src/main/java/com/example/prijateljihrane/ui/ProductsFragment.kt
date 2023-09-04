package com.example.prijateljihrane.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prijateljihrane.R
import com.example.prijateljihrane.adapters.ProductAdapter
import com.example.prijateljihrane.data.Product
import com.example.prijateljihrane.databinding.FragmentProductsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.exp

class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(layoutInflater)

        val products : ArrayList<Product> = arrayListOf()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase
            .getInstance("https://prijatelji-hrane-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")

        val adapter = ProductAdapter()
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = linearLayoutManager

        database.child(auth.currentUser?.uid.toString()).child("Products").get()
            .addOnSuccessListener { it ->
                it.children.forEach {
                    products.add(jsonConvertProduct(it.value.toString()))
                }
                adapter.setData(products.sortedByDescending { it.quantity } )
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }

        return binding.root
    }

    private fun jsonConvertProduct(jsonString: String) : Product {
        return Gson().fromJson(jsonString, Product::class.java)
    }
}