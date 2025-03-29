package com.example.recipesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Composable
fun MainScreen(viewModel: MainActivityViewModel = viewModel()) {
    val categories by viewModel.categories.collectAsState()
    println("LOGGING: Categories: $categories")

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(15.dp)) {
            Button(
                onClick = {
                    println("LOGGING: Button clicked")
                    viewModel.fetchCategories()
                },
                modifier = Modifier.fillMaxWidth().padding(15.dp)
            ) {
                Text("Load Categories")
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(15.dp)) {
                if (categories.isEmpty()) {
                    item {
                        Text("No categories available")
                    }
                } else {
                    items(categories) { category ->
                        Text(text = category.name)
                    }
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