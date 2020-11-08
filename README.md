# Diagnal

### App features:

- A simple one page app to display a list of movies from a json, lazy load them and search the data.

- The project is built using android clean architecture using MVVM, ViewModel, LiveData, and Coroutines.

- Tha data is loaded from JSON files which are stored in /assets folder, with the web services they can be loaded from external APIs using retrofit.

- The data is loaded lazily using android paging library with allows lazy loading.

- The search is performed on the app side after loading whole json, but can be replaced with APIs which accept search query params.

### Known Issues:

- Searchview is not full width