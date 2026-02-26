@echo off
echo ================================================
echo TravelEase - Travel Tourism Management System
echo ================================================

REM Check if MySQL connector JAR exists
if not exist "mysql-connector-j-9.6.0.jar" (
    echo ERROR: MySQL Connector JAR not found!
    echo Please download mysql-connector-j-9.6.0.jar from:
    echo https://dev.mysql.com/downloads/connector/j/
    echo Place it in this directory.
    pause
    exit /b 1
)

echo Step 1: Compiling Java files...
javac -cp ".;mysql-connector-j-9.6.0.jar" -d out src/travel/*.java

if errorlevel 1 (
    echo COMPILATION FAILED!
    pause
    exit /b 1
)

echo Step 2: Running application...
java -cp ".;out;mysql-connector-j-9.6.0.jar" travel.MainLandingPage

pause
