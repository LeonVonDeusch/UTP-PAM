package com.example.foodorderingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.foodorderingapp.navigation.AppNavGraph
import com.example.foodorderingapp.ui.theme.CreamBackground
import com.example.foodorderingapp.ui.theme.FoodOrderingAppTheme
import com.example.foodorderingapp.viewmodel.OrderViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FoodOrderingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CreamBackground
                ) {
                    val navController = rememberNavController()
                    AppNavGraph(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
