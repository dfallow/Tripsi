# Tripsi
### Sensor Based Mobile Applications Project - A travel journal app

Tripsi is an Android application that lets users track their trips, save the most memorable moments, and display them in a convenient way. The app uses phone's GPS sensor to track and update user's location on the map and display their saved photos in locations associated to them. Additionally, it uses a step counter to track steps taken during the trip and saves that data for future viewing. All past trips are displayed in a convenient list and each list item provides an overview of the trip and its highlights.

Application was designed and developed using Android Studio, Kotlin, Jetpack Compose, Git, and Figma.

## Features
- Two ways to start a trip:
	1. plan the trip: add date (date picker), title, destination, choose a map icon
	2. quick trip: add a title, can change the default map icon 
		- Map icon selection implemented with **ExperimentalPagerApi**
- Once the trip is started, user's location is displayed on the map. Location updates are infrequent as live location is not the main purpose of this app.
	- App uses two internal sensors:
		- **GPS sensor** tracks user's location
		- **Step sensor** counts user's steps. Steps are then displayed in an overlay on the map view along with distance that is calculated from the step count.
	- Map implemented with **osmdroid** using **mavenCentral**
- **Weather data from an API service** is displayed over the map. It shows current location weather and is updated periodically.
- User can *save moments* during their trip. That means that they can take one or multiple photos using **phone's camera** in as many locations as they want.
	- Photos are stored as jpg files in **app-specific storage**.
	- User can delete some photos before saving the *moment*. 
	- User can add a note about the *moment*.
- After adding a new *moment*, the map is updated with a new marker that shows a new location that has a photo (red circle with a photo icon). If the user clicks on one of those markers, they can view the photos they saved in that particular location.
- Once trip is ended, it appears in the trip history list. Each trip history list item displays information about the trip and its highlights. 
	- Data is fetched from **Room database**.
	- Photos are displayed in a LazyRow. On tap, they are enlarged to full size.
	- **Reverse geocoding** is used to extract start and end locations from the coordinates.
	- Trip can be deleted. That deletes all data related to that trip from Room and all the photos associated to the trip are deleted from storage.
	- Past trip route can also be viewed on a map.
### Other features
- Single Activity Architecture
- Jetpack Compose 
- Light/dark theme
- LiveData, view models
- Coroutines
- Retrofit
- Other APIs and libraries: Dagger (Hilt)

## Screenshots
![Tripsi screenshots-Group1](https://users.metropolia.fi/~gintares/Tripsi/Group%201.png)

![Tripsi screenshots-Group2](https://users.metropolia.fi/~gintares/Tripsi/Group%202.png)

![Tripsi screenshots-Group3](https://users.metropolia.fi/~gintares/Tripsi/Group%203.png)

![Tripsi screenshots-Group4](https://users.metropolia.fi/~gintares/Tripsi/Group%204.png)
## Known bugs
- Steps:
	- Steps not reset to 0 after the trip ends.
	- Steps are only saved to the database on trip end, therefore, if the app is destroyed in the middle of the trip, the step count is lost and set back to 0. 
- A feature that wasn't fully developed: past trip display on the map. Currently it shows start/end locations and saved moment locations, however, the moment (photos) is not shown when tapping on the photo icon on the map.

## Changes after review
- App crash issue identified and fixed.
- Some design changes (button colors, spacing).

## Future development ideas
- Possibility to open photos from the map view of the past trip.
- Weather data in current and destination locations.
- Notifications of an upcoming trip.
- UI/UX improvements.
- Highlights video generated from trip photos.
- Possibility to edit past trip.

## Documentation
- [GitHub project](https://github.com/users/dfallow/projects/1/views/1)
- [Figma prototype](https://www.figma.com/proto/8K4Bb49e78E6CJldCCdtuH/Prototype?node-id=14%3A9&scaling=scale-down&page-id=0%3A1&starting-point-node-id=14%3A9)

## Developers
**! Important:** Please be aware that most of [@dfallow](https://github.com/dfallow)'s  commits were not linked to his GitHub account and in the commit log they show up under a name David Fallow. He is, however, the top contributor of this project.
- [David Fallow](https://github.com/dfallow)
- [Gintare Saali](https://github.com/gintaresaali)
- [Pavlo Leinonen](https://github.com/leinonenko)
- [Ali Fahad](https://github.com/Ali-k-fahad)

## Download App
- [Tripsi](https://users.metropolia.fi/~alifah/Trips/app-debug.apk)
