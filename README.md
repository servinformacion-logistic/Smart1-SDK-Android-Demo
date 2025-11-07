# Smart1 SDK Android Demo

Smart1 SDK integration demo for Android. Shows how to initialize the SDK, use the tracker, download data, and visualize information on a map.

## About Smart1

Smart1 is a comprehensive logistics management solution for first-mile operations, from intelligent vehicle assignment to real-time trip tracking. It provides visibility and intelligent automation to reduce costs and improve efficiency in logistics centers.

The platform offers:
- **Operations Dashboard** - Real-time view of orders (assigned, in progress, completed)
- **Intelligent Route Management** - Automatic optimization based on location, dock schedules, and traffic
- **Fleet Management** - Monitoring of trucks/trailers, capacities, and predictive maintenance
- **Dynamic URLs** - Customized links to share operation status with clients

This SDK is designed for the **driver/operator side** of the logistics operation, enabling mobile applications for truck drivers who transport cargo between ports and docks, providing real-time tracking, route guidance, and order management capabilities.

## Features

- 🔑 Smart1 SDK initialization and configuration
- 📍 Real-time location tracking service
- 📦 Data retrieval (Orders, Routes, Ports, Schedules, Docks)
- 🗺️ Route and port visualization on Google Maps
- 📱 Modern UI with Jetpack Compose
- 🏗️ MVI architecture with StateFlow

## Prerequisites

Before running this project, you need:

1. **Smart1 SDK Repository Access** - Repository URL and credentials to download the SDK (provided by Smart1 administration upon SDK acquisition)
2. **Smart1 SDK API Key** - API key for SDK initialization (provided by Smart1 administration upon SDK acquisition)
3. **Smart1 User Operator Email** - Your registered operator email
4. **Google Maps API Key** - Get it from [Google Cloud Console](https://console.cloud.google.com/)

**Note:** Contact Smart1 administration to obtain the repository credentials and SDK API key.

For complete SDK documentation, visit: [Smart1 SDK Android Documentation](https://smart1-sdk-android-docs-dot-servi-smart1-logistica-dev.uc.r.appspot.com/)

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/servinformacion-logistic/Smart1SDK-Android-Demo.git
cd Smart1SDK-Android-Demo
```

### 2. Configure API Keys

Create a `local.properties` file in the root directory of the project (if it doesn't exist) and add the following properties:

```properties
# Android SDK location (automatically added by Android Studio)
sdk.dir=C\:\\Users\\YourUser\\AppData\\Local\\Android\\Sdk

# Smart1 SDK Repository Access
SDK_REPO_URL=your_sdk_repository_url
SDK_REPO_USERNAME_CREDENTIAL=your_repository_username
SDK_REPO_PASSWORD_CREDENTIAL=your_repository_password

# Google Maps API Key
GOOGLE_MAPS_API_KEY=your_google_maps_api_key_here

# Smart1 SDK Configuration
SMART1_SDK_API_KEY=your_smart1_sdk_api_key_here
SMART1_SDK_USER_OPERATOR_EMAIL=your_operator_email@example.com
```

**Important:** 
- Replace the placeholder values with your actual API keys and email
- The `local.properties` file is gitignored for security reasons
- Never commit your API keys to version control

### 3. Sync and Build

1. Open the project in Android Studio
2. Sync Gradle files
3. Build and run the project on your device or emulator

## Permissions

The app requires the following permissions:

- `ACCESS_FINE_LOCATION` - For precise location tracking
- `ACCESS_COARSE_LOCATION` - For approximate location
- `ACCESS_BACKGROUND_LOCATION` - For background location tracking (Android 10+)
- `POST_NOTIFICATIONS` - For foreground service notifications (Android 13+)
- `INTERNET` - For API communication

All permissions are requested at runtime.

## Usage

### Initialize the SDK

The SDK is automatically initialized in the `Smart1SDKDemoApp` class when the app starts:

```kotlin
val initSDKConfig = InitSDKConfig()
initSDKConfig.init(
    sdkApiKey = BuildConfig.smart1SDKApiKey,
    email     = BuildConfig.smart1SDKUserOperatorEmail,
)
```

### Start Location Tracker

Tap the **"Turn On Tracker"** button in the home screen to start the background location tracking service.

### View Orders and Routes

1. Tap the refresh button to load orders from the SDK
2. Tap **"Select a order"** to choose an order
3. The route and ports will be displayed on the map
4. Tap on markers to see port details
5. Tap the info icon to see order details

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM
- **Async:** Coroutines & Flow
- **Dependency Injection:** Koin
- **Maps:** Google Maps Compose
- **SDK:** Smart1 SDK for Android
- **Logging:** Timber

## Project Structure

```
app/
├── src/main/java/com/servinformacion/smart1sdkdemo/
│   ├── core/              # Core utilities and components
│   ├── home/              # Home screen (main feature)
│   │   ├── components/    # UI components
│   │   ├── HomeScreen.kt
│   │   ├── HomeViewModel.kt
│   │   └── HomeState.kt
│   ├── tracker/           # Location tracking service
│   └── Smart1SDKDemoApp.kt
```

## Troubleshooting

### SDK initialization error

If you see an error about empty API keys:
- Check that your `local.properties` file exists in the root directory
- Verify that all three required properties are set correctly
- Sync Gradle files again

### Map not showing

- Verify your Google Maps API Key is valid
- Enable the Maps SDK for Android in Google Cloud Console
- Check that the API key has no restrictions preventing its use

### Location tracking not working

- Ensure all location permissions are granted
- Check that location services are enabled on your device
- For background tracking, make sure background location permission is granted

## License

This is a demo project for educational purposes.

**Important:** This is not a complete, production-ready application. It serves as a starting point and reference to help understand how to use the Smart1 SDK in an Android application. The final, complete implementation depends on the specific requirements of each developer or organization integrating the SDK.
