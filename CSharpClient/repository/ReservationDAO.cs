using System;
using System.Collections.Generic;
using Microsoft.Data.Sqlite;
using model;

namespace dao
{
    public class ReservationDAO
    {
        private readonly string connectionString;

        public ReservationDAO(string connectionString)
        {
            this.connectionString = connectionString;
        }

        // Add a new Reservation
        public void Add(Reservation reservation)
        {
            Console.WriteLine($"Adding reservation: {reservation}");
            using var con = new SqliteConnection(connectionString);
            con.Open();

            string seatsStr = reservation.Seats != null ? string.Join(",", reservation.Seats) : "";

            using var cmd = new SqliteCommand(
                "INSERT INTO Reservations (IdUser, IdRace, Name, Seats) VALUES (@IdUser, @IdRace, @Name, @Seats)", con);
            cmd.Parameters.AddWithValue("@IdUser", reservation.IdUser);
            cmd.Parameters.AddWithValue("@IdRace", reservation.IdRace);
            cmd.Parameters.AddWithValue("@Name", reservation.Name);
            cmd.Parameters.AddWithValue("@Seats", seatsStr);

            int result = cmd.ExecuteNonQuery();
            Console.WriteLine($"Added {result} reservation(s)");
        }

        // Update an existing Reservation by Id
        public void Update(int id, Reservation reservation)
        {
            Console.WriteLine($"Updating reservation id {id} to {reservation}");
            using var con = new SqliteConnection(connectionString);
            con.Open();

            string seatsStr = reservation.Seats != null ? string.Join(",", reservation.Seats) : "";

            using var cmd = new SqliteCommand(
                "UPDATE Reservations SET IdUser=@IdUser, IdRace=@IdRace, Name=@Name, Seats=@Seats WHERE Id=@Id", con);
            cmd.Parameters.AddWithValue("@IdUser", reservation.IdUser);
            cmd.Parameters.AddWithValue("@IdRace", reservation.IdRace);
            cmd.Parameters.AddWithValue("@Name", reservation.Name);
            cmd.Parameters.AddWithValue("@Seats", seatsStr);
            cmd.Parameters.AddWithValue("@Id", id);

            int result = cmd.ExecuteNonQuery();
            Console.WriteLine($"Updated {result} reservation(s)");
        }

        // Delete a Reservation by Id
        public void Delete(int id)
        {
            Console.WriteLine($"Deleting reservation with id {id}");
            using var con = new SqliteConnection(connectionString);
            con.Open();

            using var cmd = new SqliteCommand("DELETE FROM Reservations WHERE Id=@Id", con);
            cmd.Parameters.AddWithValue("@Id", id);

            int result = cmd.ExecuteNonQuery();
            Console.WriteLine($"Deleted {result} reservation(s)");
        }

        // Find a Reservation by Id
        public Reservation FindById(int id)
        {
            Console.WriteLine($"Finding reservation with id {id}");
            using var con = new SqliteConnection(connectionString);
            con.Open();

            using var cmd = new SqliteCommand("SELECT * FROM Reservations WHERE Id=@Id", con);
            cmd.Parameters.AddWithValue("@Id", id);

            using var reader = cmd.ExecuteReader();
            if (reader.Read())
            {
                var seatsStr = reader.GetString(reader.GetOrdinal("Seats"));
                var seats = ParseSeats(seatsStr);

                return new Reservation(
                    reader.GetInt32(reader.GetOrdinal("Id")),
                    reader.GetInt32(reader.GetOrdinal("IdUser")),
                    reader.GetInt32(reader.GetOrdinal("IdRace")),
                    reader.GetString(reader.GetOrdinal("Name")),
                    seats
                );
            }

            return null;
        }

        // Find all Reservations
        public List<Reservation> FindAll()
        {
            Console.WriteLine("Finding all reservations");
            var reservations = new List<Reservation>();
            using var con = new SqliteConnection(connectionString);
            con.Open();

            using var cmd = new SqliteCommand("SELECT * FROM Reservations", con);
            using var reader = cmd.ExecuteReader();

            while (reader.Read())
            {
                var seatsStr = reader.GetString(reader.GetOrdinal("Seats"));
                var seats = ParseSeats(seatsStr);

                var reservation = new Reservation(
                    reader.GetInt32(reader.GetOrdinal("Id")),
                    reader.GetInt32(reader.GetOrdinal("IdUser")),
                    reader.GetInt32(reader.GetOrdinal("IdRace")),
                    reader.GetString(reader.GetOrdinal("Name")),
                    seats
                );

                reservations.Add(reservation);
            }

            Console.WriteLine($"Found {reservations.Count} reservations");
            return reservations;
        }

        // Helper to convert comma-separated string to List<int>
        private List<int> ParseSeats(string seatsStr)
        {
            var seats = new List<int>();
            if (!string.IsNullOrEmpty(seatsStr))
            {
                foreach (var s in seatsStr.Split(','))
                {
                    if (int.TryParse(s.Trim(), out int seat))
                        seats.Add(seat);
                }
            }
            return seats;
        }
    }
}