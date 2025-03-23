# Threaded Ticket System

A Java console application that simulates a concurrent ticket selling system using multithreading.

## Features

- Vendor and customer simulation with configurable rates
- Thread-safe ticket pool implementation
- JSON-based configuration saving/loading
- Activity logging to file
- Interactive CLI interface

## Technical Details

- **Language**: Java
- **Storage**: JSON configuration files
- **Classes**: Main, Configuration, Customer, Vendor, TicketPool, LogUtil
- **Dependencies**: Google Gson for JSON handling

## Setup Requirements

1. **Required Libraries**:
   - Google Gson library for JSON processing
   - Download Gson JAR file from [Maven Repository](https://mvnrepository.com/artifact/com.google.code.gson/gson)

2. **Adding Gson to your project**:
   - **IntelliJ IDEA**: 
     - Go to File → Project Structure → Libraries
     - Click the + button and select "Java"
     - Navigate to the downloaded Gson JAR file and add it
   
   - **Eclipse**:
     - Right-click on your project → Build Path → Configure Build Path
     - Click on "Libraries" tab → Add External JARs
     - Select the downloaded Gson JAR file

   - **Maven**:
     ```xml
     <dependency>
         <groupId>com.google.code.gson</groupId>
         <artifactId>gson</artifactId>
         <version>2.10.1</version>
     </dependency>
     ```
## Requirements

- JDK 11 or higher
- Gson library
