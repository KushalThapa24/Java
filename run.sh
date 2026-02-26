#!/bin/bash
echo "================================================"
echo "TravelEase - Travel Tourism Management System"
echo "================================================"

# Check for MySQL connector JAR
JAR="mysql-connector-j-8.0.33.jar"
if [ ! -f "$JAR" ]; then
    echo "ERROR: MySQL Connector JAR not found!"
    echo "Please download $JAR from:"
    echo "https://dev.mysql.com/downloads/connector/j/"
    echo "Place it in this directory."
    exit 1
fi

mkdir -p out

echo "Step 1: Compiling..."
javac -cp ".:$JAR" -d out src/travel/*.java

if [ $? -ne 0 ]; then
    echo "COMPILATION FAILED!"
    exit 1
fi

echo "Step 2: Running..."
java -cp ".:out:$JAR" travel.MainLandingPage
