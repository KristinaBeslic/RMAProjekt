package com.example.prijateljihrane.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.prijateljihrane.databinding.FragmentAddProductBinding
import com.google.zxing.integration.android.IntentIntegrator

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var qrScanIntegrator: IntentIntegrator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScanner()
        binding.scanProduct.setOnClickListener {
            setOnClickListener()
        }

        binding.addProduct.setOnClickListener {
            activity.let {
                val intent = Intent(it, AddProductInfoActivity::class.java)
                it?.startActivity(intent)
            }
        }
    }

    private fun setOnClickListener() {
        performAction()
    }

    private fun setupScanner(){
        qrScanIntegrator = IntentIntegrator.forSupportFragment(this)
        qrScanIntegrator.setOrientationLocked(true)
    }

    private fun performAction() {
        qrScanIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if(result.contents == null){
                Toast.makeText(activity, "Result not found!", Toast.LENGTH_LONG).show()
            } else{
                var result = result.contents
                Log.d("Test", result)
                val parts = result.split(";")
                val productName: String = parts[0]
                val expirationDate: String = parts[1]

                val datePickerParts = expirationDate.split(".")
                val day: Int = datePickerParts[0].toInt()
                val month: Int = datePickerParts[1].toInt()
                val year: Int = datePickerParts[2].toInt()

                val intent = Intent(activity, AddProductInfoActivity::class.java)
                val extras = Bundle()
                extras.putString("Product name", productName)
                extras.putInt("Day", day)
                extras.putInt("Month", month)
                extras.putInt("Year", year)
                intent.putExtras(extras)
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}