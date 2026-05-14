# Karunada Vanya

Kotlin Android app based on `karunada_vanya_sop1.md`.

## What is included

- Jetpack Compose UI with Splash, Home, Wildlife Encyclopedia, Wildlife Detail, Alerts, and Alert Detail screens.
- Offline-first Room database seeded with Karnataka wildlife and sample community alerts.
- Simple MVVM structure with repositories and ViewModels.
- Material 3 styling, launcher icon, Android 14 target SDK, and Android 7.0 minimum SDK.

## Open in Android Studio

1. Open `C:\Users\poorv\Desktop\FinalProject` as an Android Studio project.
2. Let Android Studio sync Gradle.
3. Run the `app` configuration on an emulator or Android phone.

## Firebase next step

Real-time alerts are wired for Firebase Realtime Database. Add your Firebase Android app config at:

`app/google-services.json`

The app writes alert posts under:

`alerts/{pushId}` with `animalType`, `location`, and `timestamp`.

The feed listens to the same node and filters alerts in Kotlin so only sightings from the last 6 hours are visible.
