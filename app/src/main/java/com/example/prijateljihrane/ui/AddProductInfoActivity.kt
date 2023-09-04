package com.example.prijateljihrane.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.prijateljihrane.data.Product
import com.example.prijateljihrane.R
import com.example.prijateljihrane.databinding.ActivityAddProductInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddProductInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductInfoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase
            .getInstance("https://prijatelji-hrane-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")

        val intent: Intent = intent
        val extras: Bundle? = intent.extras

        var name = "Apple"
        var quantity = 3
        var date = "25.12.2023"

        binding.saveProductButton.isEnabled = false
        binding.productNameEt.addTextChangedListener(textWatcher)

        if (extras != null) {
            val productName = extras.getString("Product name")
            binding.productNameEt.setText(productName)
            val day = extras.getInt("Day")
            val month = extras.getInt("Month")
            val year = extras.getInt("Year")

            date = getString(R.string.product_expiration_date, day.toString(),month.toString(),year.toString())

            binding.datePicker.updateDate(year, month - 1, day)
        } else {
            setMinDate()
        }
        setQuantity()

        binding.saveProductButton.setOnClickListener {

            name = binding.productNameEt.text.toString()
            quantity = binding.quantityEt.text.toString().toInt()

            val day: Int = binding.datePicker.dayOfMonth
            val month: Int = binding.datePicker.month + 1
            val year: Int = binding.datePicker.year
            date = day.toString() + "." + month.toString() + "." + year.toString()

            val product = Product(
                name ,
                quantity,
                date
            )
            database.child(auth.currentUser?.uid.toString()).child("Products")
                .child(UUID.randomUUID().toString()).setValue(product).addOnSuccessListener {
                Toast.makeText(this, "Product saved!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Product isn't saved!", Toast.LENGTH_SHORT).show()
            }

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

    }

    private val textWatcher: TextWatcher = object: TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.run{
                saveProductButton.isEnabled = validateProductName()
                productNameLayout.helperText =
                    if(!validateProductName())
                        resources.getString(R.string.required)
                    else null
            }
        }
        override fun afterTextChanged(p0: Editable?) = Unit

    }

    private fun setQuantity() {
        binding.quantityEt.setText("1")
        binding.decreaseBtn.setOnClickListener {
            decreaseQuantity()
        }
        binding.increaseBtn.setOnClickListener {
            increaseQuantity()
        }
    }

    private fun decreaseQuantity() {
        val quantityValueEt = binding.quantityEt.text.toString()

        if (quantityValueEt.toInt() == 1) {
            binding.decreaseBtn.isEnabled = false
        } else {
            val quantity: Int = quantityValueEt.toInt() - 1
            binding.quantityEt.setText(quantity.toString())
        }
    }

    private fun increaseQuantity() {
        val quantityValueEt = binding.quantityEt.text.toString()
        val quantity: Int = quantityValueEt.toInt() + 1
        if (quantity > 1)
            binding.decreaseBtn.isEnabled = true
        binding.quantityEt.setText(quantity.toString())
    }

    private fun setMinDate() {
        binding.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    private fun validateProductName(): Boolean{
        return binding.productNameEt.text.toString() != ""
    }

    private fun validateQuantity(): Boolean{
        if(binding.quantityEt.text.toString().toInt() < 1)
            Toast.makeText(this, "Quantity must be larger than 0!", Toast.LENGTH_LONG).show()
        return binding.quantityEt.text.toString().toInt() >= 1
    }
}
