package com.example.recipesearchapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainActivityViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val mealsList by viewModel.mealsList.collectAsState()
    val errorMessage by viewModel.errorMsg.collectAsState()

    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            if (query.length == 1) {
                viewModel.searchMealsByFirstLetter(query)
            } else {
                viewModel.searchMeals(query)
            }
        } else {
            viewModel.clearSearchResults()
        }
    }

    Scaffold {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    active = false
                    if (query.isNotEmpty()) {
                        if (query.length == 1) {
                            viewModel.searchMealsByFirstLetter(query)
                        } else {
                            viewModel.searchMeals(query)
                        }
                    }
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(text = "Search for a meal") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon") },
                trailingIcon = {
                    if (active) {
                        Icon(
                            modifier = Modifier.clickable {
                                if (query.isNotEmpty()) {
                                    query = ""
                                } else {
                                    active = false
                                }
                            },
                            imageVector = Icons.Default.Close, contentDescription = "Close Icon"
                        )
                    }
                }
            ) {
                when {
                    !errorMessage.isNullOrEmpty() -> {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    mealsList.isEmpty() && query.isNotEmpty() -> {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "No meals found."
                        )
                    }
                    else -> {
                        MealListScreen(mealsList)
                    }
                }
            }
        }
    }
}

@Composable
fun MealListScreen(meals: List<Meal>) {
    LazyColumn (
        Modifier.padding(top = 15.dp)
    ) {
        items(meals) { meal ->
            MealItem(meal)
        }
    }
}

@Composable
fun MealItem(meal: Meal) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${meal.area} - ${meal.category}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecipeSearchAppTheme {
        MainScreen()
    }
}