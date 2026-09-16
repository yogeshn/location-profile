# Smart GPS

Smart GPS is a legacy Android application that combines several location‑aware utilities into a single, lightweight app:

- **Navigation / Map view** using Google Maps Android API v2
- **Track Me** – periodic GPS logging and map markers
- **Pedometer** – step counter functionality
- **Location‑based alarms** – trigger an alarm when you are near a destination
- **Settings & Help** screens for basic configuration and guidance

> This repository is a legacy Android project that targets Google Play services and Google Maps v2. It is structured as an Ant/Eclipse Android project rather than a modern Gradle project.

---

## Project Overview

### Key Features

- Display your current position on a Google Map
- Continuously track and record locations over time
- Show recorded positions in a list and on a map
- Raise an alarm when you approach a configured destination
- Pedometer view with step count and status information
- Basic settings and help/about entries

### Main Components (Activities)

The core screens are implemented as Android `Activity` classes under `src/com/location/smartGPS/`:

- `SpalshScreenActivity` – app splash screen and launcher activity
- `MainActivity`, `MainActivitynavigation`, `MainActivityold` – main navigation / home activities
- `TrackMeActivity` – continuous GPS tracking
- `pedometerActivity` – step counter
- `SetAlarm`, `LocAlarmActivity`, `Alarm_map`, `AlarmStarted`, `Alarmdone` – location alarm configuration and execution
- `MapaActivity`, `LlistaLocationsActivity`, `Gridactivity` – map display, list of locations, and grid menu
- `GPSSettingActivity` – GPS and network location settings helper

The overall application metadata and entry points are defined in `AndroidManifest.xml`.

---

## Requirements

Because this is an older Android project, it uses the Android Ant/Eclipse project layout and Google Play services as a library project.

### Android SDK

- **Minimum SDK version:** 11 (Android 3.0, Honeycomb)
- **Target SDK version:** 17 (Android 4.2, Jelly Bean)
- **Target platform:** `Google Inc.:Google APIs:17` (see `project.properties`)

### Permissions

The app requires a number of permissions to provide mapping, tracking, and alarm functionality (see `AndroidManifest.xml`):

- Location access: `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`
- Network access: `INTERNET`, `ACCESS_NETWORK_STATE`, `CHANGE_WIFI_STATE`
- Storage: `WRITE_EXTERNAL_STORAGE`
- Phone: `CALL_PHONE`
- System / services: `RECEIVE_BOOT_COMPLETED`, `com.google.android.providers.gsf.permission.READ_GSERVICES`

If you distribute this application, you should clearly communicate why these permissions are needed and how location data is used (see [Privacy & Data](#privacy--data)).

### External Dependencies

- **Google Play services / Google Maps Android API v2**
  - Declared via `com.google.android.gms.version` metadata and a Maps API key in `AndroidManifest.xml`
  - Linked as a library reference in `project.properties` (`google-play-services_lib`)

You will need a valid **Google Maps Android API v2 key** for your own package name and signing certificate if you plan to build and run a forked version.

---

## Getting Started

> These instructions assume you are familiar with legacy Android Ant/Eclipse projects. For modern development, you may prefer to migrate the project to Gradle/Android Studio.

### 1. Clone the repository

```bash
git clone &lt;this-repo-url&gt;
cd smart-gps
```

### 2. Set up the Android SDK

1. Install the Android SDK and platform tools.
2. Using the SDK Manager, install:
   - Android 4.2 (API 17) platform
   - Google APIs for API 17
   - Google Play services (and its support libraries)

### 3. Import the project

#### Option A – Eclipse / ADT (legacy)

1. Import the project as an **Existing Android Application**.
2. Ensure the `google-play-services_lib` project is present in your workspace and referenced correctly (see `project.properties`).
3. Clean and build the workspace.

#### Option B – Android Studio (migrate)

This repository is not a Gradle project by default.

To work with Android Studio you can either:

- Use *Import project (Eclipse ADT, Gradle, etc.)* and let Android Studio attempt to migrate it; or
- Manually create a new Gradle project and copy the source (`src/`), resources (`res/`), and manifest into the new project, then wire up Google Play services as a Gradle dependency.

Migration details are outside the scope of this README but should follow standard Android migration practices.

### 4. Configure Google Maps API key

In `AndroidManifest.xml`, the Maps API key is provided via a `<meta-data android:name="com.google.android.maps.v2.API_KEY" ... />` entry. For your own builds you should:

1. Generate a new API key in the Google Cloud Console for the **Maps SDK for Android**.
2. Restrict the key to your application package and SHA‑1 signing certificate.
3. Replace the existing key value in the manifest with your own.

---

## Running the App

Once the project is imported and successfully built:

1. Connect an Android device (or start an emulator) that has Google Play services installed.
2. Deploy the app from your IDE.
3. Grant the requested permissions on first run, if prompted.
4. From the splash screen, navigate to the desired feature (Navigation, Track Me, Pedometer, Alarms, Settings, Help, About).

Some features (such as tracking and location‑based alarms) require that **GPS or network location providers** be enabled in system settings.

---

## Configuration

Configuration is done primarily through:

- **In‑app settings screens** (e.g., GPS settings helper)
- UI controls for tracking interval, destination address, alarm radius, and alarm tone

Details of these options are implemented in the activities under `src/com/location/smartGPS/` and associated resource strings in `res/values/strings.xml`.

---

## Privacy & Data

Smart GPS works with precise location data and may write information such as tracked positions to local storage.

If you plan to modify or redistribute this project, consider the following:

- **Explain data usage clearly** – how frequently locations are recorded and for what purpose.
- **Storage scope** – where positions are stored (e.g., internal database, text files on external storage) and how long they are kept.
- **No implicit data transmission** – the original project is designed primarily for on‑device use; if you add any network transmission of location data, document and secure it appropriately.

You may also want to provide a dedicated privacy policy alongside this README for app stores or public distribution.

---

## Project Structure

At a high level, the repository is organized as follows:

- `AndroidManifest.xml` – application manifest, activities, permissions, and metadata
- `project.properties` – Ant / Android build properties and library references
- `src/com/location/smartGPS/` – Java source code for all activities and helpers
- `res/` – resources (layouts, values, drawables, etc.)
- `gen/` – generated resources (do not edit)
- `LICENSE.md` – license for the project (Apache License 2.0)
- `README.md` – this document

---

## Contributing

This is a legacy project and may require updates to run on modern Android devices and tooling. If you plan to contribute:

1. Fork the repository.
2. Make your changes in a feature branch.
3. Follow standard Android coding and resource naming conventions.
4. Test on at least one real device with Google Play services.
5. Open a pull request describing your changes and the Android versions/devices you tested.

---

## License

This project is licensed under the **Apache License, Version 2.0**.

See [`LICENSE.md`](LICENSE.md) for the full license text and copyright notice.

---

## Acknowledgements

- Original author: **Yogesh Nitnaware** (see license header in `LICENSE.md`).
- Google Maps Android API v2 and Google Play services for mapping and location APIs.

Additional details about specific screens and workflows can be expanded in future iterations of this README as the codebase is refactored or modernized.
