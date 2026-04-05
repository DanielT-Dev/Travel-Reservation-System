using System;
using System.Collections.Generic;

namespace model
{
    public class Reservation
    {
        public int? Id { get; set; }
        public int? IdUser { get; set; }
        public int? IdRace { get; set; }
        public string Name { get; set; }
        public List<int> Seats { get; set; }

        // Added for DataGrid display in the WPF UI
        public string SeatsDisplay => Seats != null ? string.Join(", ", Seats) : "—";

        // Constructor with Id
        public Reservation(int? id, int? idUser, int? idRace, string name, List<int> seats)
        {
            Id = id;
            IdUser = idUser;
            IdRace = idRace;
            Name = name;
            Seats = seats;
        }

        // Constructor without Id
        public Reservation(int? idUser, int? idRace, string name, List<int> seats)
        {
            IdUser = idUser;
            IdRace = idRace;
            Name = name;
            Seats = seats;
        }

        public override string ToString()
        {
            string seatsStr = Seats != null ? string.Join(", ", Seats) : "null";
            return $"{Id} {Name} {IdUser} {IdRace} [{seatsStr}]";
        }
    }
}