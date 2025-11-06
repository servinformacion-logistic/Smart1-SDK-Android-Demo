pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        val properties = java.util.Properties()
        val localPropertiesFile = java.io.File(rootDir, "local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(java.io.FileInputStream(localPropertiesFile))
        }
        
        val sdkRepoUrl = properties.getProperty("SDK_REPO_URL")
        if (!sdkRepoUrl.isNullOrEmpty()) {
            maven {
                url = uri(sdkRepoUrl)
                credentials {
                    username = properties.getProperty("SDK_REPO_USERNAME_CREDENTIAL")
                    password = properties.getProperty("SDK_REPO_PASSWORD_CREDENTIAL")
                }
            }
        }
    }
}

rootProject.name = "Smart 1 SDK Demo"
include(":app")
 