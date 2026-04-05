package server;

import network.ClientHandler;
import repository.RaceDAO;
import repository.ReservationDAO;
import repository.UserDAO;
import service.RaceService;
import service.ReservationService;
import service.UserService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppServer {

    private static final int PORT = 5000;
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        RaceDAO raceDAO = new RaceDAO();
        ReservationDAO reservationDAO = new ReservationDAO();

        UserService userService = new UserService(userDAO);
        RaceService raceService = new RaceService(raceDAO);
        ReservationService reservationService = new ReservationService(reservationDAO);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                try {
                    System.out.println("Waiting for client connection...");
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected from: " + socket.getInetAddress() + ":" + socket.getPort());

                    ClientHandler handler = new ClientHandler(
                            socket,
                            userService,
                            raceService,
                            reservationService,
                            clients
                    );

                    clients.add(handler);
                    new Thread(handler).start();

                    System.out.println("Handler thread started for this client.");
                } catch (IOException e) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void remove(ClientHandler handler) {
        clients.remove(handler);
    }
}