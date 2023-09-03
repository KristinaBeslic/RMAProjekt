package com.example.prijateljihrane.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.prijateljihrane.R
import com.example.prijateljihrane.data.Product
import com.example.prijateljihrane.databinding.ProductItemBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val products: MutableList<Product?> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val context = holder.itemView.context

        holder.binding.productName.text = context.getString(R.string.product_info, product?.name)
        holder.binding.quantityInfo.text = context.getString(R.string.quantity_info, product?.quantity)
        holder.binding.expirationDate.text = context.getString(R.string.expiration_date, product?.expirationDate)

        holder.binding.deleteProductButton.setOnClickListener{
            Toast.makeText(context, "Product deleted!", Toast.LENGTH_SHORT).show()
            products.removeAt(position)
            notifyItemChanged(position)
        }


    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ProductViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun setData(products: List<Product?>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }
}