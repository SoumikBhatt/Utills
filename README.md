# Utills
![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat) [![](https://jitpack.io/v/SoumikBhatt/ProToast.svg)](https://jitpack.io/#SoumikBhatt/ProToast)

All Utility functions in one library

## Prerequisites

Add this in your project level `build.gradle` file :

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

## Dependency

Add this to your module's `build.gradle` file :

```gradle
dependencies {
	...
	implementation 'com.github.SoumikBhatt:Utills:1.0.1'
}
```

## Usage

After adding the dependency, you can start using various `Utils` straight way.

To use **Share** Util :

```kotlin
Utills.share(yourContext,"subject","message","chooser title")
``` 

To use **Feedback** Util :

```kotlin
Utills.feedback(yourContext,"email_id of reciever","subject","message")
``` 

To use **Rating (on Play store)** Util :

```kotlin
Utills.rateApp(yourContext)
``` 

To use **FB Like** Util :

```kotlin
Utills.likeOnFB(yourContext,"pageID","pageUserName")
``` 
