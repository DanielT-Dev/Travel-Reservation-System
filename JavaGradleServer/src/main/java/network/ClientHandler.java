package network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Race;
import model.Reservation;
import model.User;
import server.AppServer;
import service.RaceService;
import service.ReservationService;
import service.UserService;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final UserService userService;
    private final RaceService raceService;
    private final ReservationService reservationService;
    private final List<ClientHandler> clientHandlers;
    private final Gson gson = new Gson();

    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket clientSocket, UserService userService, RaceService raceService,
                         ReservationService reservationService, List<ClientHandler> clientHandlers) {
        this.clientSocket = clientSocket;
        this.userService = userService;
        this.raceService = raceService;
        this.reservationService = reservationService;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            System.out.println("Handler ready to receive commands from: " + clientSocket.getInetAddress());

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received command: " + line);
                handleCommand(line);
            }

            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.err.println("Handler error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            AppServer.remove(this);
            try {
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void handleCommand(String cmd) throws IOException {
        String[] parts = cmd.split("\\|", 4);

        switch (parts[0]) {
            case "LOGIN" -> {
                System.out.println("Processing LOGIN for: " + parts[1]);
                boolean success = userService.login(parts[1], parts[2]).isPresent();
                System.out.println("Login success: " + success);
                send(success ? "SUCCESS" : "FAIL");
            }

            case "GET_USERS" -> {
                List<User> users = userService.getAllUsers();
                send(gson.toJson(users));
            }

            case "GET_RACES" -> {
                List<Race> races = raceService.findAll();
                send(gson.toJson(races));
            }

            case "GET_RESERVATIONS" -> {
                List<Reservation> res = reservationService.findAll();
                send(gson.toJson(res));
            }

            case "FIND_RACE" -> {
                Race race = raceService.findByDetails(parts[1], parts[2], parts[3]);
                send(gson.toJson(race));
            }

            case "GET_SEATS" -> {
                int raceId = Integer.parseInt(parts[1]);
                send(gson.toJson(reservationService.getSeatsForRace(raceId)));
            }

            case "GET_FREE_SEATS" -> {
                int raceId = Integer.parseInt(parts[1]);
                send(gson.toJson(reservationService.getFreeSeats(raceId)));
            }

            case "CREATE_RESERVATION" -> {
                int rId = Integer.parseInt(parts[1]);
                String clientName = parts[2];

                Type seatsType = new TypeToken<List<Integer>>() {}.getType();
                List<Integer> selectedSeats = gson.fromJson(parts[3], seatsType);

                reservationService.createReservation(rId, clientName, selectedSeats);

                send("OK");
                notifyAllClients();
            }

            case "CANCEL_RESERVATION" -> {
                int resId = Integer.parseInt(parts[1]);
                reservationService.deleteReservation(resId);

                send("OK");
                notifyAllClients();
            }

            default -> send("UNKNOWN_COMMAND");
        }
    }

    private void notifyAllClients() {
        for (ClientHandler client : clientHandlers) {
            client.send("UPDATE");
        }
    }

    public void send(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}