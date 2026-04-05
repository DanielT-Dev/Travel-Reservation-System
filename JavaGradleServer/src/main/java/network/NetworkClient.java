package network;

import com.google.gson.Gson;
import javafx.application.Platform;
import model.Race;
import model.Reservation;
import model.SeatDTO;
import model.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkClient {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Gson gson = new Gson();

    private final BlockingQueue<String> responseQueue = new LinkedBlockingQueue<>();
    private final List<Runnable> dataChangedListeners = new CopyOnWriteArrayList<>();

    public NetworkClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        startReaderThread();
    }

    private void startReaderThread() {
        Thread readerThread = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if ("UPDATE".equals(line)) {
                        Platform.runLater(() -> {
                            for (Runnable listener : dataChangedListeners) {
                                listener.run();
                            }
                        });
                    } else {
                        responseQueue.offer(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "network-reader");

        readerThread.setDaemon(true);
        readerThread.start();
    }

    public void onDataChanged(Runnable listener) {
        dataChangedListeners.add(listener);
    }

    private synchronized String sendAndWait(String command) {
        try {
            out.write(command);
            out.newLine();
            out.flush();
            return responseQueue.take();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public boolean login(String email, String password) {
        String response = sendAndWait("LOGIN|" + email + "|" + password);
        return "SUCCESS".equals(response);
    }

    public List<User> getAllUsers() {
        String json = sendAndWait("GET_USERS");
        if (json == null || json.isBlank()) return new ArrayList<>();
        User[] users = gson.fromJson(json, User[].class);
        return users == null ? new ArrayList<>() : Arrays.asList(users);
    }

    public List<Race> getAllRaces() {
        String json = sendAndWait("GET_RACES");
        if (json == null || json.isBlank()) return new ArrayList<>();
        Race[] races = gson.fromJson(json, Race[].class);
        return races == null ? new ArrayList<>() : Arrays.asList(races);
    }

    public List<Reservation> getAllReservations() {
        String json = sendAndWait("GET_RESERVATIONS");
        if (json == null || json.isBlank()) return new ArrayList<>();
        Reservation[] reservations = gson.fromJson(json, Reservation[].class);
        return reservations == null ? new ArrayList<>() : Arrays.asList(reservations);
    }

    public Race findRace(String dest, String date, String time) {
        String json = sendAndWait("FIND_RACE|" + dest + "|" + date + "|" + time);
        if (json == null || json.isBlank() || "null".equals(json)) return null;
        return gson.fromJson(json, Race.class);
    }

    public List<SeatDTO> getSeatsForRace(int raceId) {
        String json = sendAndWait("GET_SEATS|" + raceId);
        if (json == null || json.isBlank()) return new ArrayList<>();

        SeatDTO[] seats = gson.fromJson(json, SeatDTO[].class);
        return seats == null ? new ArrayList<>() : Arrays.asList(seats);
    }

    public List<Integer> getFreeSeats(int raceId) {
        String json = sendAndWait("GET_FREE_SEATS|" + raceId);
        if (json == null || json.isBlank()) return new ArrayList<>();

        Integer[] seats = gson.fromJson(json, Integer[].class);
        return seats == null ? new ArrayList<>() : Arrays.asList(seats);
    }

    public void createReservation(int raceId, String name, List<Integer> selectedSeats) {
        String seatsJson = gson.toJson(selectedSeats);
        sendAndWait("CREATE_RESERVATION|" + raceId + "|" + name + "|" + seatsJson);
    }

    public void cancelReservation(int reservationId) {
        sendAndWait("CANCEL_RESERVATION|" + reservationId);
    }
}