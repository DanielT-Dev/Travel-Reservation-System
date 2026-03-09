using System;
using System.Collections.Generic;
using Microsoft.Data.Sqlite;
using model;

namespace dao
{
    public class RaceDAO
    {
        private readonly string connectionString;

        public RaceDAO(string connectionString)
        {
            this.connectionString = connectionString;
        }

        // Add a new Race
        public void Add(Race race)
        {
            Console.WriteLine($"Adding race: {race}");
            using var con = new SqliteConnection(connectionString);
            con.Open();

            string seatsStr = race.AvaiableSeats != null ? string.Join(",", race.AvaiableSeats.ConvertAll(b => b ? "1" : "0")) : "";

            using var cmd = new SqliteCommand(
                "INSERT INTO Races (Destination, Date, Time, AvaiableSeats) VALUES (@Destination, @Date, @Time, @Seats)", con);
            cmd.Parameters.AddWithValue("@Destination", race.Destination);
            cmd.Parameters.AddWithValue("@Date", race.Date);
            cmd.Parameters.AddWithValue("@Time", race.Time);
            cmd.Parameters.AddWithValue("@Seats", seatsStr);

            int result = cmd.ExecuteNonQuery();
            Console.WriteLine($"Added {result} race(s)");
        }

        // Update an existing Race by Id
        public void Update(int id, Race race)
        {
            Console.WriteLine($"Updating race id {id} to {race}");
            using var con = new SqliteConnection(connectionString);
            con.Open();

            string seatsStr = race.AvaiableSeats != null ? string.Join(",", race.AvaiableSeats.ConvertAll(b => b ? "1" : "0")) : "";

            using var cmd = new SqliteCommand(
                "UPDATE Races SET Destination = @Destination, Date = @Date, Time = @Time, AvaiableSeats = @Seats WHERE Id = @Id", con);
            cmd.Parameters.AddWithValue("@Destination", race.Destination);
            cmd.Parameters.AddWithValue("@Date", race.Date);
            cmd.Parameters.AddWithValue("@Time", race.Time);
            cmd.Parameters.AddWithValue("@Seats", seatsStr);
            cmd.Parameters.AddWithValue("@Id", id);

            int result = cmd.ExecuteNonQuery();
            Console.WriteLine($"Updated {result} race(s)");
        }

        // Delete a Race by Id
        public void Delete(int id)
        {
            Console.WriteLine($"Deleting race with id {id}");
            using var con = new SqliteConnection(connectionString);
            con.Open();

            using var cmd = new SqliteCommand("DELETE FROM Races WHERE Id = @Id", con);
            cmd.Parameters.AddWithValue("@Id", id);

            int result = cmd.ExecuteNonQuery();
            Console.WriteLine($"Deleted {result} race(s)");
        }

        // Find a Race by Id
        public Race FindById(int id)
        {
            Console.WriteLine($"Finding race with id {id}");
            using var con = new SqliteConnection(connectionString);
            con.Open();

            using var cmd = new SqliteCommand("SELECT * FROM Races WHERE Id = @Id", con);
            cmd.Parameters.AddWithValue("@Id", id);

            using var reader = cmd.ExecuteReader();
            if (reader.Read())
            {
                var seatsStr = reader.GetString(reader.GetOrdinal("AvaiableSeats"));
                var seats = ParseSeats(seatsStr);

                return new Race(
                    reader.GetInt32(reader.GetOrdinal("Id")),
                    reader.GetString(reader.GetOrdinal("Destination")),
                    reader.GetString(reader.GetOrdinal("Date")),
                    reader.GetString(reader.GetOrdinal("Time")),
                    seats
                );
            }

            return null;
        }

        // Find all Races
        public List<Race> FindAll()
        {
            Console.WriteLine("Finding all races");
            var races = new List<Race>();
            using var con = new SqliteConnection(connectionString);
            con.Open();

            using var cmd = new SqliteCommand("SELECT * FROM Races", con);
            using var reader = cmd.ExecuteReader();

            while (reader.Read())
            {
                var seatsStr = reader.GetString(reader.GetOrdinal("AvaiableSeats"));
                var seats = ParseSeats(seatsStr);

                var race = new Race(
                    reader.GetInt32(reader.GetOrdinal("Id")),
                    reader.GetString(reader.GetOrdinal("Destination")),
                    reader.GetString(reader.GetOrdinal("Date")),
                    reader.GetString(reader.GetOrdinal("Time")),
                    seats
                );
                races.Add(race);
            }

            Console.WriteLine($"Found {races.Count} races");
            return races;
        }

        // Helper to convert comma-separated 0/1 string to List<bool>
        private List<bool> ParseSeats(string seatsStr)
        {
            var seats = new List<bool>();
            if (!string.IsNullOrEmpty(seatsStr))
            {
                foreach (var s in seatsStr.Split(','))
                {
                    seats.Add(s.Trim() == "1");
                }
            }
            return seats;
        }
    }
}