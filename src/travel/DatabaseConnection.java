package travel;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/travel_tourism";
    private static final String USER = "root";
    private static final String PASSWORD = "kushal123"; // Change to your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        String createDB = "CREATE DATABASE IF NOT EXISTS travel_tourism";
        String useDB = "USE travel_tourism";

        String createOwners = """
            CREATE TABLE IF NOT EXISTS hotel_owners (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL,
                full_name VARCHAR(200) NOT NULL,
                email VARCHAR(200) NOT NULL
            )
        """;

        String createCustomers = """
            CREATE TABLE IF NOT EXISTS customers (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL,
                full_name VARCHAR(200) NOT NULL,
                email VARCHAR(200) NOT NULL,
                phone VARCHAR(20)
            )
        """;

        String createHotels = """
            CREATE TABLE IF NOT EXISTS hotels (
                id INT AUTO_INCREMENT PRIMARY KEY,
                owner_id INT NOT NULL,
                name VARCHAR(200) NOT NULL,
                location VARCHAR(300) NOT NULL,
                description TEXT,
                price_per_night DECIMAL(10,2) NOT NULL,
                available_rooms INT NOT NULL,
                total_rooms INT NOT NULL,
                rating DECIMAL(3,1) DEFAULT 4.0,
                image_path VARCHAR(500),
                amenities VARCHAR(500),
                FOREIGN KEY (owner_id) REFERENCES hotel_owners(id)
            )
        """;

        String createBookings = """
            CREATE TABLE IF NOT EXISTS bookings (
                id INT AUTO_INCREMENT PRIMARY KEY,
                customer_id INT NOT NULL,
                hotel_id INT NOT NULL,
                check_in DATE NOT NULL,
                check_out DATE NOT NULL,
                num_rooms INT NOT NULL,
                total_price DECIMAL(10,2) NOT NULL,
                booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                status VARCHAR(50) DEFAULT 'CONFIRMED',
                FOREIGN KEY (customer_id) REFERENCES customers(id),
                FOREIGN KEY (hotel_id) REFERENCES hotels(id)
            )
        """;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", PASSWORD)) {
            conn.createStatement().execute(createDB);
            conn.createStatement().execute(useDB);
            conn.createStatement().execute(createOwners);
            conn.createStatement().execute(createCustomers);
            conn.createStatement().execute(createHotels);
            conn.createStatement().execute(createBookings);
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}
