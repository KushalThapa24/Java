# ✈ TravelEase - Travel & Tourism Management System

A full-featured Java Swing desktop application with MySQL database integration for managing hotel listings and bookings.

---

## 📋 Features

### 🏠 Landing Page
- Animated scenic tourist destination background (ocean, mountains, palm trees, clouds)
- Two login portals: **Traveler** and **Hotel Owner**

### 🏨 Hotel Owner Portal
- Register & Login
- **Add Hotels**: Name, Location, Description, Price/Night, Number of Rooms, Photos, Amenities
- **Edit** existing hotel listings
- **Delete** hotels
- "Read More" expandable description on each hotel card
- View available vs. total rooms in real-time

### 👤 Customer Portal
- Register & Login
- **Search hotels** by Name or Location
- Browse hotel cards with images, ratings, prices, room availability
- "Read More" to see full description + amenities
- **Book hotels**: Select check-in/check-out dates, number of rooms
- Automatic room count deduction on successful booking
- View **My Bookings** history with all details

### 🗄️ Database (MySQL)
- `hotel_owners` - Owner accounts
- `customers` - Customer accounts
- `hotels` - Hotel listings (linked to owners)
- `bookings` - Booking records (linked to customers + hotels)
- **Atomic booking**: Room count decrements in same transaction as booking insert

---

## 🚀 Setup Instructions

### Prerequisites
1. **Java JDK 17+** - Download from https://adoptium.net
2. **MySQL 8.0+** - Download from https://dev.mysql.com/downloads/mysql/
3. **MySQL Connector/J** - Download from https://dev.mysql.com/downloads/connector/j/
   - Choose "Platform Independent" → download the ZIP → extract `mysql-connector-j-8.0.33.jar`

### Step 1: Database Setup
```sql
-- Open MySQL command line or MySQL Workbench and run:
mysql -u root -p < database_setup.sql

-- OR copy-paste the contents of database_setup.sql into MySQL Workbench
```

### Step 2: Configure Database Password
Open `src/travel/DatabaseConnection.java` and update line 5:
```java
private static final String PASSWORD = "your_mysql_password_here";
```

### Step 3: Place MySQL JAR
Put `mysql-connector-j-8.0.33.jar` in the `TravelTourism/` root folder (same folder as `run.bat`).

### Step 4: Run the Application

**Windows:**
```
double-click run.bat
```

**Linux/Mac:**
```bash
chmod +x run.sh
./run.sh
```

**Manual compile & run:**
```bash
mkdir out
javac -cp ".:mysql-connector-j-8.0.33.jar" -d out src/travel/*.java
java -cp ".:out:mysql-connector-j-8.0.33.jar" travel.MainLandingPage
```

---

## 👥 Sample Login Credentials
After running `database_setup.sql`:

| Role | Username | Password |
|------|----------|----------|
| Hotel Owner | `owner1` | `pass123` |
| Traveler | `traveler1` | `pass123` |

---

## 📁 Project Structure
```
TravelTourism/
├── src/travel/
│   ├── MainLandingPage.java     # Landing page with animated tourist background
│   ├── LoginPage.java           # Login/Register for both roles
│   ├── OwnerDashboard.java      # Hotel owner management panel
│   ├── CustomerDashboard.java   # Customer search & booking panel
│   ├── WrapLayout.java          # Helper for responsive card grid
│   └── DatabaseConnection.java  # MySQL connection & DB init
├── database_setup.sql           # Run this first to set up MySQL
├── run.bat                      # Windows launcher
├── run.sh                       # Linux/Mac launcher
└── README.md
```

---

## 🔧 How Booking Works
1. Customer searches for hotels by name or location
2. Clicks "Book Now" on a hotel card
3. Enters check-in date, check-out date, and number of rooms
4. System confirms booking details with total cost
5. On confirmation:
   - A `bookings` record is inserted into the database
   - `hotels.available_rooms` is decremented by the number of rooms booked
   - Both operations happen in a **single database transaction** (atomic)
6. Hotel card immediately shows the updated room count

---

## 📸 Adding Hotel Photos
When adding a hotel as an owner:
1. Click "Browse Image" button
2. Select a JPG or PNG image from your computer
3. The full file path is stored in the database
4. Images render directly on hotel cards in the customer view

*Note: For the best experience, use images around 800x500 pixels.*
