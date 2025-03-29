# Recipe Search App

For CS501 - Mobile App Development class. Uses MVVM architecture.

## Features

- **Search**: Users can search for recipes by query (e.g., "chicken", "pasta").
- **API Integration**: Fetches data from [The Meal DB API](https://www.themealdb.com/api.php).
- **LazyColumn**: Displays recipe title, image, and description.
- **State Management**: Uses `StateFlow` for handling loading, data, and error states.
- **Loading & Error**: Displays a loading indicator and error messages.

## Technologies

- **Retrofit** for API calls
- **Moshi** for JSON parsing
- **OkHttp** as HTTP client
- **Coil** for image loading
- **Jetpack Compose** for UI
- **Flow/StateFlow** for state management
