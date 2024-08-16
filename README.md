# CustomExoVideoPlayer
1. Gradle Dependency
* Add the JitPack repository to your project's build.gradle file
    - For Gradle version 5.x.x or less

   ```kotlin
        allprojects {
            repositories {
                ...
    	         maven { url = "https://jitpack.io" }
            }
        }
   ```
    - For latest Android Studio, in **settings.gradle** file
      inside **`dependencyResolutionManagement`** block

   ```kotlin
        dependencyResolutionManagement {
            repositories {
                ...
                 maven { url = "https://jitpack.io" }
            }
        }
    ```
* Add the dependency in your app's build.gradle file

    ```kotlin
        dependencies {
	        implementation 'com.github.NavjotSinghSeraphic:CustomExoVideoPlayer:1.0'
        }
    ```
