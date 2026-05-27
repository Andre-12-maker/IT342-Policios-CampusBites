package com.example.campusbites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.campusbites.core.navigation.AppNavGraph
import com.example.campusbites.shared.theme.CampusBitesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusBitesTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}