# A221002_Aizulfadhli_Bin_Zainuddin_MR_Nelson_Project2
A native Android health-tracking application built with Kotlin and Jetpack Compose that implements a local-remote data sync architecture, asynchronous REST API networking, and hardware location mapping to support SDG 3 (Good Health & Well-being).


# CareGo: Connected Student Health Ecosystem (SDG 3)

### Student Profile
* **Student Name:** Aizulfadhli Bin Zainuddin
* **Student ID:** A221002
* **Selected SDG:** Goal 3: Good Health and Well-being

---

## The Problem Statement for Project 2

### 1. The Core Problem
University students in Malaysia experience high academic pressure, resulting in irregular sleep, stress, and unmanaged physical health symptoms. While the initial iteration of CareGo addressed low-friction data logging via dynamic UI inputs, it suffered from framework-level limitations: 
* **State Transience:** Closing app sessions or experiencing configuration lifecycle changes cleared unsaved state variables, rendering historical health trends completely untrackable.
* **Isolation of Data:** The application operated as an offline silo with no data backup, no access to external professional guidance, and an inability to provide physical, real-world utility during sudden medical emergencies.

### 2. The Impact (Why it matters)
Without persistent tracking, student health trends remain invisible, leading to a lack of long-term self-awareness and delayed medical intervention. Neglecting minor signs increases the risk of academic burnout and long-term physical illness. Furthermore, an isolated health application fails to protect a student when they require immediate, real-world medical resources or actionable health advice, leaving a critical gap in fulfilling the core pillars of **SDG 3: Good Health and Well-being**.

### 3. The Engineering Solution: The Connected CareGo Ecosystem
Project 2 transitions CareGo from a transient tracking utility into a resilient, cloud-connected mobile health ecosystem. Built using **Kotlin** and **Jetpack Compose**, it implements an asynchronous architecture built upon four core engineering pillars to bridge the gap between digital tracking and real-world intervention:

* **Pillar 1: Persistent Offline Data Logging (Room SQLite Database)**
  Replaces volatile view-scoped arrays with a robust local SQLite infrastructure. Using Data Access Objects (DAOs), user health logs, metrics, and symptoms are safely preserved via asynchronous CRUD operations, making historical health trends fully retrievable even without internet connectivity.
  
* **Pillar 2: Resilient Cloud Synchronization (Firebase Firestore)**
  Establishes a continuous real-time background sync pipeline. Local user data pools are mirrored onto a remote cloud database, protecting against device failures and ensuring data consistency across multiple client instances.
  
* **Pillar 3: Dynamic Knowledge Delivery (Retrofit & GSON REST API)**
  Integrates a non-blocking asynchronous networking client. By communicating with external REST API endpoints, the system fetches, parses, and surfaces real-time wellness advice, equipping students with actionable health recommendations on-demand.
  
* **Pillar 4: On-Device Hardware Sensor Mapping (GPS & Implicit Intents)**
  Integrates physical location-aware services through the `FusedLocationProviderClient`. In the event of an urgent health decline or physical emergency, the application captures device GPS coordinates and triggers a native implicit system intent to immediately route the student to the nearest medical facilities.

---

## Tech Stack Matrix
* **Language:** Kotlin Core
* **UI Framework:** Jetpack Compose (Declarative Component Tree Layouts)
* **Architecture:** MVVM Pattern with Unidirectional State Flow (`MutableStateFlow`)
* **Local Storage:** Room Persistence Library (SQLite Abstraction)
* **Networking:** Retrofit HTTP Client + GSON Converter Factory
* **Cloud Infrastructure:** Firebase Firestore NoSQL Database
* **Hardware Integration:** Google Play Services Location API (GPS Tracking) & Implicit Intent Routing
