package com.example.foodorderingapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.foodorderingapp.model.FoodItem

class OrderViewModel : ViewModel() {

    val menuItems = mutableStateListOf(
        FoodItem(
            id = 1,
            name = "Nasi Goreng Spesial",
            price = 25000.0,
            description = "Nasi goreng dengan bumbu rempah pilihan, dilengkapi telur mata sapi, ayam suwir, dan kerupuk renyah. Cocok untuk makan siang maupun malam.",
            emoji = "🍳",
            category = "Makanan Utama"
        ),
        FoodItem(
            id = 2,
            name = "Mie Ayam Bakso",
            price = 20000.0,
            description = "Mie kenyal dengan topping ayam cincang berbumbu, bakso sapi pilihan, dan kuah kaldu gurih yang menyegarkan.",
            emoji = "🍜",
            category = "Makanan Utama"
        ),
        FoodItem(
            id = 3,
            name = "Ayam Bakar Madu",
            price = 35000.0,
            description = "Ayam kampung pilihan yang dibakar dengan bumbu madu dan kecap, disajikan dengan lalapan segar dan sambal terasi.",
            emoji = "🍗",
            category = "Makanan Utama"
        ),
        FoodItem(
            id = 4,
            name = "Soto Betawi",
            price = 28000.0,
            description = "Soto khas Betawi dengan kuah santan gurih, isian daging sapi empuk, kentang, dan tomat. Dilengkapi emping dan kerupuk.",
            emoji = "🥘",
            category = "Makanan Utama"
        ),
        FoodItem(
            id = 5,
            name = "Gado-Gado",
            price = 18000.0,
            description = "Sayuran segar rebus dengan saus kacang yang kaya rasa, dilengkapi telur rebus, tahu, tempe, dan kerupuk.",
            emoji = "🥗",
            category = "Sayuran"
        ),
        FoodItem(
            id = 6,
            name = "Sate Kambing",
            price = 45000.0,
            description = "Sate kambing pilihan yang empuk dan tidak prengus, dilengkapi bumbu kacang spesial dan lontong.",
            emoji = "🍢",
            category = "Panggang"
        ),
        FoodItem(
            id = 7,
            name = "Es Teh Manis",
            price = 8000.0,
            description = "Teh manis segar dengan es batu yang menyegarkan, pilihan sempurna untuk menemani makanan Anda.",
            emoji = "🧊",
            category = "Minuman"
        ),
        FoodItem(
            id = 8,
            name = "Jus Alpukat",
            price = 15000.0,
            description = "Jus alpukat segar dengan susu cokelat dan sirup gula merah, creamy dan mengenyangkan.",
            emoji = "🥑",
            category = "Minuman"
        ),
        FoodItem(
            id = 9,
            name = "Pisang Goreng Cokelat",
            price = 12000.0,
            description = "Pisang kepok yang digoreng sempurna dengan taburan cokelat dan keju parut, cocok sebagai cemilan sore.",
            emoji = "🍌",
            category = "Dessert"
        ),
        FoodItem(
            id = 10,
            name = "Es Campur",
            price = 14000.0,
            description = "Minuman segar dengan aneka buah, cincau, nata de coco, dan sirup merah dengan susu kental manis.",
            emoji = "🍧",
            category = "Dessert"
        )
    )

    val totalItems: Int
        get() = menuItems.sumOf { it.quantity }

    val totalPrice: Double
        get() = menuItems.sumOf { it.price * it.quantity }

    fun increaseQuantity(itemId: Int) {
        val index = menuItems.indexOfFirst { it.id == itemId }
        if (index != -1) {
            menuItems[index] = menuItems[index].copy(quantity = menuItems[index].quantity + 1)
        }
    }

    fun decreaseQuantity(itemId: Int) {
        val index = menuItems.indexOfFirst { it.id == itemId }
        if (index != -1 && menuItems[index].quantity > 0) {
            menuItems[index] = menuItems[index].copy(quantity = menuItems[index].quantity - 1)
        }
    }

    fun getItemById(itemId: Int): FoodItem? {
        return menuItems.find { it.id == itemId }
    }

    fun formatPrice(price: Double): String {
        return "Rp ${String.format("%,.0f", price).replace(',', '.')}"
    }
}
