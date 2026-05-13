package com.example.foodorderingapp.model

data class FoodItem(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val emoji: String,
    val category: String,
    var quantity: Int = 0
)
