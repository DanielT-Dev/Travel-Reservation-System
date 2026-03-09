using System;
using System.Collections.Generic;
using Microsoft.Extensions.Configuration;
using dao;
using model;
using Microsoft.Data.Sqlite;

void CreateTables(string connectionString)
{
    using var con = new SqliteConnection(connectionString);
    con.Open();

    var cmd = con.CreateCommand();

    cmd.CommandText = @"
    CREATE TABLE IF NOT EXISTS Users (
        Id INTEGER PRIMARY KEY AUTOINCREMENT,
        Name TEXT NOT NULL,
        Email TEXT NOT NULL,
        Password TEXT NOT NULL
    );

    CREATE TABLE IF NOT EXISTS Races (
        Id INTEGER PRIMARY KEY AUTOINCREMENT,
        Destination TEXT NOT NULL,
        Date TEXT NOT NULL,
        Time TEXT NOT NULL,
        AvaiableSeats TEXT
    );

    CREATE TABLE IF NOT EXISTS Reservations (
        Id INTEGER PRIMARY KEY AUTOINCREMENT,
        IdUser INTEGER,
        IdRace INTEGER,
        Name TEXT NOT NULL,
        Seats TEXT
    );
    ";

    cmd.ExecuteNonQuery();
}

// Load configuration
var builder = new ConfigurationBuilder()
    .SetBasePath(AppContext.BaseDirectory)
    .AddJsonFile("appsettings.json");

var config = builder.Build();

string raceConn = config.GetConnectionString("RaceDB");
string userConn = config.GetConnectionString("UserDB");
string reservationConn = config.GetConnectionString("ReservationDB");

// Create tables
// CreateTables(userConn);
// CreateTables(raceConn);
// CreateTables(reservationConn);

// Create DAOs
var raceDAO = new RaceDAO(raceConn);
var userDAO = new UserDAO(userConn);
var reservationDAO = new ReservationDAO(reservationConn);

// Test UserDAO
var user = new User("Alice", "alice@example.com", "password123");
userDAO.Add(user);

var allUsers = userDAO.FindAll();
Console.WriteLine("Users in DB:");
foreach (var u in allUsers)
    Console.WriteLine(u);