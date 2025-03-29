package com.example.recipesearchapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipesearchapp.ui.theme.RecipeSearchAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

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
    val isLoading by viewModel.isLoading.collectAsState()

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
                placeholder = { Text(text = "Search. Click on meal for more") },
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
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    !errorMessage.isNullOrEmpty() -> {
                        Text(
                            modifier = Modifier.padding(15.dp),
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    mealsList.isEmpty() && query.isNotEmpty() -> {
                        Text(
                            modifier = Modifier.padding(15.dp),
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
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { isExpanded = !isExpanded },
        shape = MaterialTheme.shapes.medium,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(5f)
                ) {
                    Text(
                        text = meal.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "${meal.area} • ${meal.category}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                AsyncImage(
                    model = meal.picURL,
                    contentDescription = "Meal image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(5.dp))
                )
            }

            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Text(
                        text = meal.instructions,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
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