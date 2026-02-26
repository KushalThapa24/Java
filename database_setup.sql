-- ================================================
-- TravelEase - Travel & Tourism Management System
-- Database Setup Script
-- ================================================

CREATE DATABASE IF NOT EXISTS travel_tourism;
USE travel_tourism;

-- Hotel Owners Table
CREATE TABLE IF NOT EXISTS hotel_owners (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customers Table
CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hotels Table
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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES hotel_owners(id) ON DELETE CASCADE
);

-- Bookings Table
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
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE
);

-- ============ Sample Data ============

-- Sample Hotel Owner
INSERT IGNORE INTO hotel_owners (username, password, full_name, email)
VALUES ('owner1', 'pass123', 'John Smith', 'john@hotel.com');

-- Sample Customer
INSERT IGNORE INTO customers (username, password, full_name, email, phone)
VALUES ('traveler1', 'pass123', 'Alice Johnson', 'alice@email.com', '+1-555-0101');

-- Sample Hotels (linked to owner_id 1)
INSERT IGNORE INTO hotels (owner_id, name, location, description, price_per_night, available_rooms, total_rooms, rating, amenities)
VALUES 
(1, 'Paradise Beach Resort', 'Bali, Indonesia', 
 'A stunning beachfront resort with breathtaking ocean views. Perfect for couples and families seeking a tropical getaway with world-class amenities and authentic Balinese hospitality.',
 120.00, 15, 20, 4.8,
 'Free WiFi, Swimming Pool, Spa, Beach Access, Restaurant, Room Service, Air Conditioning'),

(1, 'Mountain View Lodge', 'Pokhara, Nepal', 
 'Nestled in the Himalayan foothills with panoramic mountain views. Experience the serenity of the Annapurna range from our cozy, well-appointed rooms. Ideal for trekkers and nature lovers.',
 75.00, 8, 12, 4.6,
 'Free WiFi, Breakfast Included, Trekking Guides, Mountain Views, Garden, Yoga Classes'),

(1, 'City Grand Hotel', 'Paris, France', 
 'Elegant luxury hotel in the heart of Paris, just minutes from the Eiffel Tower. Classic French decor, fine dining, and impeccable service for the discerning traveler.',
 250.00, 5, 30, 4.9,
 'Free WiFi, Fine Dining, Concierge, Gym, Rooftop Bar, Valet Parking, Business Center');

-- Verify setup
SELECT 'Database setup complete!' as Status;
SELECT 'Tables created:' as Info;
SHOW TABLES;
