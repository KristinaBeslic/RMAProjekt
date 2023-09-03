package com.example.prijateljihrane.data

import java.util.*

data class Product(val name: String, val quantity: Int, val expirationDate: String) {
    override fun toString(): String {
        return "$name-$quantity-$expirationDate"
    }
}
