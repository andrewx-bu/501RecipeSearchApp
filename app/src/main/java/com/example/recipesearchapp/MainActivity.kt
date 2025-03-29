package com.example.recipesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipesearchapp.ui.theme.RecipeSearchAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeSearchAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainActivityViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val mealsList by viewModel.mealsList.collectAsState()
    val errorMessage by viewModel.errorMsg.collectAsState()

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                if (query.length == 1) {
                    viewModel.searchMealsByFirstLetter(query)
                } else if (query.isNotEmpty()) {
                    viewModel.searchMeals(query)
                }
            },
            label = { Text("Search for a meal") },
        )

        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage!!,
            )
        } else if (mealsList.isEmpty() && query.isNotEmpty()) {
            Text(
                text = "No meals found.",
            )
        } else {
            MealListScreen(mealsList)
        }
    }
}

@Composable
fun MealListScreen(meals: List<Meal>) {
    LazyColumn {
        items(meals) { meal ->
            MealItem(meal)
        }
    }
}

@Composable
fun MealItem(meal: Meal) {
    Card {
        Text(
            text = meal.name
        )
        Text(
            text = "${meal.area} - ${meal.category}"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecipeSearchAppTheme {
        MainScreen()
    }
}