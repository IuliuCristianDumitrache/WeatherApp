# Weather ReadMe

WeatherApp provides users with real-time weather information for their current location as well as any other location they choose. Users can create a widget for quick access, save favorite locations, or view them on a map.

<img src="https://github.com/IuliuCristianDumitrache/WeatherApp/blob/main/fullflow.gif" width="400" height="790">

## Requirements
- Implement one of the two designs: the Forest design or the Sea design.
- Fetch weather data based on the user’s current location using the following APIs:
  - [OpenWeatherMap Current API](https://openweathermap.org/current)
  - [OpenWeatherMap 5-day Forecast API](https://openweathermap.org/forecast5)
- Change background images dynamically based on weather conditions (Cloudy, Sunny, Rainy).
- Demonstrate best practices including:
  - Architecture
  - Design Patterns
  - Unit Testing (preferentially TDD)
  - Proper use of SOLID principles
  - Integration into a CI/CD build pipeline
  - Code coverage integration
  - Static code analysis
- Additional features:
  - Save favorite locations
  - View list of favorites
  - Get extra information about a specific location using Google Places API
  - View weather information while offline
  - View saved locations on a map

## Design Chosen
The design chosen was the Forest design. The application connects to OpenWeatherMap API to fetch weather data and utilizes the /current endpoint for current weather and the /forecast5 endpoint for 5-day forecast. It dynamically changes background images to reflect real-time weather conditions.

## Features
- **Favorite Locations:** Users can save and view favorite locations for easy access.
- **Map View:** Users can visualize favorite locations on a map.
- **Search:** Utilizes the Places API to search for weather conditions in any location worldwide.
- **Settings:** Change the unit of measurement from metric to imperial.
- **Offline Support:** Enables viewing weather information while offline, displaying the time of the last update.
- **Widget:** Possibility to add widget on home screen with current location weather condition and quick access to the app / refresh button.

## Architecture
The WeatherApp follows the Model-View-ViewModel (MVVM) architecture pattern for better separation of concerns and maintainability. Components include:
- **Model:** Represents data and business logic.
- **View:** Represents UI components and layout.
- **ViewModel:** Acts as a mediator between Model and View, handling UI-related logic.

## Continuous Integration/Continuous Deployment (CI/CD)
Implemented CI/CD pipelines using GitHub Actions to automate build, test, and deployment processes.

## Unit Testing
Implement CI/CD pipelines using GitHub Actions to automate build, test, and deployment processes.

There are two actions on GitHub, one to Build Debug APK and one to run Unit Tests.  
You can manually run the Build Debug APK action to generate an artifact.

UnitTest action contains the static code analysis ( Lint ) and test coverage artifacts. This action will run on every push from the main branch or can be manually trigerred.

## Code Coverage Integration
Utilized code coverage tools like JaCoCo to measure code coverage and ensure thorough testing.

## Third-party Libraries
1. **Retrofit2:**
   - *Implementation:* com.squareup.retrofit2:retrofit:2.9.0
     - Retrofit is a type-safe HTTP client for Android and Java used to make API calls in a structured and organized manner.
   - *Implementation:* com.squareup.retrofit2:converter-gson:2.9.0
     - Gson converter for Retrofit, used for converting JSON response from API calls to Java/Kotlin objects.
   
2. **OkHttp Logging interceptor:**
   - *Implementation:* com.squareup.okhttp3:logging-interceptor:4.9.1
     - OkHttp is an HTTP client for Java and Kotlin. The logging interceptor is used for logging HTTP request and response data for debugging purposes.
   
3. **Dagger Hilt:**
   - *Implementation:* com.google.dagger:hilt-android:2.44
     - Dagger Hilt is a dependency injection library for Android. It simplifies the process of managing dependencies and facilitates modularization.
   - *Implementation:* androidx.hilt:hilt-navigation-compose:1.1.0
     - Hilt Navigation Compose provides integration between Jetpack Navigation and Hilt for dependency injection in Jetpack Compose-based applications.
   - *Kapt:* com.google.dagger:hilt-android-compiler:2.44, androidx.hilt:hilt-compiler:1.1.0
     - Annotation processors for Dagger Hilt to generate code for dependency injection.
   - *Implementation:* androidx.hilt:hilt-work:1.1.0
     - Hilt Work provides support for dependency injection in Android WorkManager classes.
   
4. **WorkManager:**
   - *Implementation:* androidx.work:work-runtime-ktx:2.9.0
     - WorkManager is a library used for performing background tasks in Android applications, providing features like periodic tasks, constraints, and observability.
   
5. **ViewModel:**
   - *Implementation:* androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0
     - ViewModel component of the Android Architecture Components used for managing UI-related data in a lifecycle-conscious way.
   - *Implementation:* androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0, androidx.lifecycle:lifecycle-runtime-compose:2.7.0
     - ViewModel utilities for integration with Jetpack Compose.
   
6. **Security Crypto:**
   - *Implementation:* androidx.security:security-crypto-ktx:1.1.0-alpha06
     - Provides cryptographic functionality for secure storage and retrieval of sensitive data.
   
7. **Room:**
   - *Kapt:* androidx.room:room-compiler:2.4.2
     - Annotation processor for Room database to generate boilerplate code.
   - *Implementation:* androidx.room:room-ktx:2.4.2, androidx.room:room-runtime:2.4.2
     - Room persistence library for database management in Android applications.
   
8. **Glance:**
   - *Implementation:* androidx.glance:glance-appwidget:1.0.0, androidx.glance:glance-material3:1.0.0
     - Libraries for AppWidgets support and interop APIs with Material 3.
   
9. **Coil:**
   - *Implementation:* io.coil-kt:coil-compose:2.4.0
     - Image loading library for Android applications, specifically designed for Jetpack Compose.
   
10. **Android Maps Compose:**
    - *Implementation:* com.google.maps.android:maps-compose:4.3.0
      - Composables for integrating Google Maps SDK with Jetpack Compose.
    - *Implementation:* com.google.android.libraries.places:places:3.3.0
      - Google Places API for location search functionality.
    
11. **Constraint Layout:**
    - *Implementation:* androidx.constraintlayout:constraintlayout-compose:1.0.1
      - ConstraintLayout library for building complex UI layouts in Jetpack Compose.
    
12. **Testing:**
    - *TestImplementation:* io.mockk:mockk:1.13.9
      - Mocking library for Kotlin used in unit testing to simulate behavior of objects and dependencies.


## Building the Project
### Prerequisites
- Android Studio installed on your development machine.
- OpenWeatherMap API key for fetching weather data.
- Google API key for places and maps.

### Steps
1. Clone the repository from GitHub.
2. Open the project in Android Studio.
3. Replace placeholder API keys in the local properties configuration file (local.properties).
4. Build and run the project on your desired Android device or emulator.

## Sources
- [Refresh icon](https://www.flaticon.com/free-icon/refresh_2805355?term=refresh&page=1&position=1&origin=search&related_id=2805355)
- [Like icon](https://www.flaticon.com/free-icon/like_535183?related_id=535183)
