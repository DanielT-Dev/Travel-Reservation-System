using System.Data;
using model;
using System.Data.SQLite;

namespace dao
{
    public class UserDAO
    {
        private readonly string connectionString;

        public UserDAO(string connectionString)
        {
            this.connectionString = connectionString;
        }

        // Add a new user
        public void Add(User user)
        {
            Console.WriteLine($"Adding user: {user}");
            using var con = new SQLiteConnection(connectionString);
            con.Open();

            using var cmd = new SQLiteCommand(
                "INSERT INTO Users (Name, Email, Password) VALUES (@Name, @Email, @Password)", con);
            cmd.Parameters.AddWithValue("@Name", user.Name);
            cmd.Parameters.AddWithValue("@Email", user.Email);
            cmd.Parameters.AddWithValue("@Password", user.Password);

            int result = cmd.ExecuteNonQuery();
            Console.WriteLine($"Added {result} user(s)");
        }

        // Update an existing user by Id
        public void Update(int id, User user)
        {
            Console.WriteLine($"Updating user id {id} to {user}");
            using var con = new SQLiteConnection(connectionString);
            con.Open();

            using var cmd = new SQLiteCommand(
                "UPDATE Users SET Name = @Name, Email = @Email, Password = @Password WHERE Id = @Id", con);
            cmd.Parameters.AddWithValue("@Name", user.Name);
            cmd.Parameters.AddWithValue("@Email", user.Email);
            cmd.Parameters.AddWithValue("@Password", user.Password);
            cmd.Parameters.AddWithValue("@Id", id);

            int result = cmd.ExecuteNonQuery();
            Console.WriteLine($"Updated {result} user(s)");
        }

        // Delete a user by Id
        public void Delete(int id)
        {
            Console.WriteLine($"Deleting user with id {id}");
            using var con = new SQLiteConnection(connectionString);
            con.Open();

            using var cmd = new SQLiteCommand("DELETE FROM Users WHERE Id = @Id", con);
            cmd.Parameters.AddWithValue("@Id", id);

            int result = cmd.ExecuteNonQuery();
            Console.WriteLine($"Deleted {result} user(s)");
        }

        // Find a user by Id
        public User FindById(int id)
        {
            Console.WriteLine($"Finding user with id {id}");
            using var con = new SQLiteConnection(connectionString);
            con.Open();

            using var cmd = new SQLiteCommand("SELECT * FROM Users WHERE Id = @Id", con);
            cmd.Parameters.AddWithValue("@Id", id);

            using SQLiteDataReader reader = cmd.ExecuteReader();
            if (reader.Read())
            {
                return new User(
                    reader.GetInt32(reader.GetOrdinal("Id")),
                    reader.GetString(reader.GetOrdinal("Name")),
                    reader.GetString(reader.GetOrdinal("Email")),
                    reader.GetString(reader.GetOrdinal("Password"))
                );
            }

            return null;
        }

        // Find all users
        public List<User> FindAll()
        {
            Console.WriteLine("Finding all users");
            var users = new List<User>();
            using var con = new SQLiteConnection(connectionString);
            con.Open();

            using var cmd = new SQLiteCommand("SELECT * FROM Users", con);
            using SQLiteDataReader reader = cmd.ExecuteReader();

            while (reader.Read())
            {
                var user = new User(
                    reader.GetInt32(reader.GetOrdinal("Id")),
                    reader.GetString(reader.GetOrdinal("Name")),
                    reader.GetString(reader.GetOrdinal("Email")),
                    reader.GetString(reader.GetOrdinal("Password"))
                );
                users.Add(user);
            }

            Console.WriteLine($"Found {users.Count} users");
            return users;
        }
    }
}