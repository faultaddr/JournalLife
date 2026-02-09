# Notebook Journal - Compose Multiplatform Application

This is a complete Compose Multiplatform application based on the Notebook Journal specification. The application supports Android, iOS, and Desktop platforms.

## Features Implemented

- **User Management**: Login/register functionality
- **Journal Creation**: Create and edit journal entries with text and images
- **Book Organization**: Organize journals into books
- **Privacy Controls**: Set journals as public or private
- **Statistics**: View writing frequency, word counts, and other metrics
- **Navigation**: Tab-based navigation between different sections

## Project Structure

```
app/
├── src/
│   ├── commonMain/
│   │   ├── kotlin/
│   │   │   └── com/example/journal/
│   │   │       ├── models/          # Data models
│   │   │       ├── ui/              # UI components
│   │   │       │   └── theme/       # Theme definitions
│   │   │       ├── navigation/      # Navigation logic
│   │   │       ├── screens/         # Screen implementations
│   │   │       ├── repository/      # Data repository
│   │   │       ├── viewmodel/       # ViewModels
│   │   │       └── di/              # Dependency injection
│   │   └── composeResources/        # Compose resources
│   ├── androidMain/
│   ├── iosMain/
│   └── desktopMain/
```

## How to Build and Run

### Prerequisites

1. Java 11 or higher
2. Gradle 8.4 or higher
3. Android Studio/IntelliJ IDEA for Android development
4. Xcode for iOS development (macOS only)

### Setup Instructions

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-name>
   ```

2. Build the project:
   ```bash
   # For Android
   ./gradlew assembleDebug
   
   # For Desktop
   ./gradlew run
   
   # For iOS (requires macOS and Xcode)
   ./gradlew build
   ```

### Using Android Studio/IntelliJ IDEA

1. Open the project in Android Studio or IntelliJ IDEA
2. Import the Gradle project
3. Wait for dependencies to download
4. Run the application on your chosen target platform

## Architecture

The application follows clean architecture principles:

- **Presentation Layer**: Compose UI and ViewModels
- **Domain Layer**: Business logic and use cases
- **Data Layer**: Repository implementations and data sources

## Technologies Used

- Kotlin Multiplatform
- Compose Multiplatform
- Voyager (Navigation)
- Koin (Dependency Injection)
- Kotlin Coroutines
- Kotlin DateTime

## Data Models

The application implements all data models as specified in the requirements:
- User
- Book
- JournalEntry
- Block (TextBlock, ImageBlock, etc.)
- Media
- Share

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.