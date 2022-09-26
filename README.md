# Tango products demo application

Overengineered app for skill demonstration that allows to view a product list and search for product.

## Screenshots

<img src="images/screenshot_start.png" width="200"/> <img src="images/screenshot_search.png" width="200"/>
<img src="images/screenshot_search_fail.png" width="200"/> <img src="images/screenshot_search_dark.png" width="200"/>

## Stack

- Jetpack Compose
- MVVM
- Coroutines + Flow
- Dagger 2
- Retrofit

## Modules

<img src="images/deps_graph.png" alt="Dependency graph" width="500"/>

### [base/app](sources/base/app)

Contains Application, root Activity, dagger application component. Provides dependencies for features.

### [base/core](sources/base/core)

Contains domain models, interfaces that should be provided to features.

### [base/datasource](sources/base/datasource)

Contains data-providing logic: repositories implementations, paging sources, api generation.

### [base/designsystem](sources/base/designsystem)

Contains theme, color palette and common design elements.

### [base/navigation](sources/base/navigation)

Kotlin module. Contains keys and parameters for navigation graph.

### [features/productlist](sources/features/productlist)

Contains composed UI, ViewModel, dagger component for feature.

## Code quality tasks

`./gradlew test` — runs all the unit tests in the project

`./gradlew detektAll` — runs detekt checks for the whole project

## Download

[Release APK](https://github.com/Yundin/TangoDemo/releases/tag/1.1.0)

## Author

**Vladislav Yundin** /
4yundin@gmail.com /
[Resume](https://github.com/Yundin/resume/blob/master/resume_eng.pdf) /
[LinkedIn](https://www.linkedin.com/in/vladislav-yundin-74774b18a/)
