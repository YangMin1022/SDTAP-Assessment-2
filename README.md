# Software Development Tools and Practices Assessment 2
This project consists of two main components:

- Part A – Web-Service API: A Spring Boot-based API that exposes endpoints for retrieving photographic gallery data.

- Part B – Swing Client: A Java Swing desktop application that consumes the API to display data.

This README provides instructions to set up, build, run, and test the project from a fresh environment.

## Table of Contents
- [Prerequisites](#prerequisites)

- [Project Setup](#project-setup)

- [Running the Application](#running-the-application)
  - [Part A – Web-Service API](#part-a---web-service-api)
  - [Part B – Swing Client](#part-b---swing-client)

- [Running Tests](#running-tests)

- [Generating Code Coverage Report](#generating-code-coverage-report)

- [Project Structure](#project-structure)

## Prerequisites
Before setting up the project, ensure you have the following installed:

- **Java Development Kit (JDK):** Version 19 (or as specified in your project).

- **Gradle:** The project uses Gradle as the build tool. You can use the Gradle wrapper provided (`gradlew`/`gradlew.bat`).

- **Git:** For cloning the repository.

- **An IDE:** VS Code, IntelliJ IDEA, or Eclipse are recommended for Java development.

- **Internet Connection:** For Gradle to download dependencies.

## Project Setup
1. **Clone the Repository:**

   ```bash
   git clone [INSERT_GITHUB_REPOSITORY_URL]
   cd [PROJECT_DIRECTORY]

2. **Open the Project:**
   - **VS Code:**
     Open VS Code, choose File > Open Folder..., and select the cloned project folder.
   - **IntelliJ IDEA/Eclipse:**
     Import the project as a Gradle project.

3. **Resolve Dependencies:**
   Gradle will automatically download all required dependencies. You can trigger this by running:

   ```bash
   ./gradlew 

  This command compiles the project and downloads necessary dependencies.

## Running the Application
### Part A – Web-Service API
1. **Run Using Gradle Wrapper:**
   Open a terminal in the project directory and run:
   ```bash
   ./gradlew bootRun

2. **Access the API:**
   The API will start on the default port (8080). You can test it by navigating to:
   - Get photos by artist:
     http://localhost:8080/api/photos/artist?name=Smith
   - Get top-rated photos:
     http://localhost:8080/api/photos/top-rated 
   - Get the most popular rating day:
     http://localhost:8080/api/ratings/most-popular-day
   - Get average rating for an artist:
     http://localhost:8080/api/photos/average-rating?artist=Smith

### Part B – Swing Client
Run the Swing Client:

Open the main class for the Swing client (e.g., GalleryClient.java) in your IDE and run the main() method.
Alternatively, you can package it into a JAR and run:

bash

java -jar [PATH_TO_YOUR_JAR]

Using the Client:

The Swing client GUI will appear. You can enter an artist's name (e.g., "Mike") and click the "Fetch Photos" button to retrieve data from the API.

## Running Tests
To run both unit and integration tests:

Open a terminal in the project directory.

Execute the following command:

bash

./gradlew test

This command runs all tests in the src/test/java directory.

## Generating Code Coverage Report
The project is set up with JaCoCo to generate code coverage reports.

Run the tests along with the coverage report generation:

bash

./gradlew test jacocoTestReport

Open the HTML report by navigating to:

build/reports/jacoco/test/html/index.html

in your browser.

## Project Structure

bash

.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com/example/tapassessment
│   │   │       ├── controller
│   │   │       │   └── PhotoController.java
│   │   │       ├── model
│   │   │       │   ├── Member.java
│   │   │       │   ├── Photo.java
│   │   │       │   └── Rating.java
│   │   │       └── service
│   │   │           └── PhotographicGalleryService.java
│   │   └── resources
│   └── test
│       └── java
│           └── com/example/tapassessment
│               ├── controller
│               │   └── PhotoControllerIntegrationTest.java
│               └── service
│                   └── PhotographicGalleryServiceTest.java
├── build.gradle
└── README.md
