package com.cit.policios.campusbites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private var selectedCategory: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeScrollView = findViewById<ScrollView>(R.id.homeScrollView)
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val userInfoText = findViewById<TextView>(R.id.userInfoText)
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val viewMenuButton = findViewById<Button>(R.id.viewMenuButton)
        val exploreSection = findViewById<LinearLayout>(R.id.exploreSection)

        val userId = intent.getStringExtra("user_id") ?: ""
        val name = intent.getStringExtra("user_name") ?: "CampusBites User"
        val email = intent.getStringExtra("user_email") ?: ""


        welcomeText.text = "Welcome, $name!"
        userInfoText.text = email

        viewMenuButton.setOnClickListener {
            homeScrollView.post {
                homeScrollView.smoothScrollTo(0, exploreSection.top)
            }
        }

        val categories = mapOf(
            R.id.catSalad to "Salad",
            R.id.catRolls to "Rolls",
            R.id.catDeserts to "Deserts",
            R.id.catSandwich to "Sandwich",
            R.id.catCake to "Cake",
            R.id.catPureVeg to "Pure Veg",
            R.id.catPasta to "Pasta",
            R.id.catNoodles to "Noodles"
        )

        Thread {
            val result = ApiClient.getFoods()
            runOnUiThread {
                if (result.isSuccess) {
                    val foods = result.getOrNull() ?: emptyList()
                    // TODO: pass foods to a RecyclerView adapter
                    // For now, log count:
                    android.util.Log.d("CampusBites", "Loaded ${foods.size} food items")
                } else {
                    android.util.Log.e("CampusBites", "Food load failed: ${result.exceptionOrNull()?.message}")
                }
            }
        }.start()

        categories.forEach { (viewId, categoryName) ->
            findViewById<LinearLayout>(viewId).setOnClickListener {
                selectedCategory = if (selectedCategory == categoryName) "All" else categoryName
                updateCategorySelection(categories)
            }
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }



    private fun updateCategorySelection(categories: Map<Int, String>) {
        categories.forEach { (viewId, categoryName) ->
            val container = findViewById<LinearLayout>(viewId)
            val imageView = container.getChildAt(0)
            if (categoryName == selectedCategory) {
                imageView.setBackgroundResource(R.drawable.circle_outline_active)
            } else {
                imageView.setBackgroundResource(R.drawable.circle_outline)
            }
        }
    }
}