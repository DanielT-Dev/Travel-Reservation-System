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


var user1 = new User(1, "Alice Popescu", "alice.popescu@email.com", "password123");

var user2 = new User(2, "Mihai Ionescu", "mihai.ionescu@email.com", "qwerty456");

var user3 = new User(3, "Elena Georgescu", "elena.georgescu@email.com", "hello789");

var user4 = new User("Andrei Dumitrescu", "andrei.dumitrescu@email.com", "securePass1");

var user5 = new User("Maria Stan", "maria.stan@email.com", "myPassword42");

userDAO.Add(user1);
userDAO.Add(user2);
userDAO.Add(user3);
userDAO.Add(user4);
userDAO.Add(user5);

var race1 = new Race(1, "Cluj-Napoca", "2026-04-10", "08:30",
    new List<bool> { true, true, false, true, false, true, true, false, true, true, false, true, true, true, false, true, false, true });

var race2 = new Race(2, "Bucharest", "2026-04-11", "14:00",
    new List<bool> { true, true, true, true, true, false, true, true, false, true, true, true, false, true, true, true, true, false });

var race3 = new Race(3, "Timișoara", "2026-04-12", "09:45",
    new List<bool> { false, false, true, true, false, true, true, false, true, false, true, true, true, false, true, true, false, true });

var race4 = new Race("Iași", "2026-04-13", "17:20",
    new List<bool> { true, false, true, false, true, true, false, true, true, false, true, true, false, true, true, false, true, true });

var race5 = new Race("Constanța", "2026-04-14", "06:10",
    new List<bool> { true, true, false, false, true, true, true, false, true, false, true, true, false, true, false, true, true, true });

// raceDAO.Add(race1);
// raceDAO.Add(race2);
// raceDAO.Add(race3);
// raceDAO.Add(race4);
// raceDAO.Add(race5);


var allUsers = userDAO.FindAll();
Console.WriteLine("Users in DB:");
foreach (var u in allUsers)
    Console.WriteLine(u);