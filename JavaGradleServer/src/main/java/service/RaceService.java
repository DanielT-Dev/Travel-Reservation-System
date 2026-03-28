package service;

import model.Race;
import repository.RaceDAO;

import java.util.ArrayList;
import java.util.List;

public class RaceService {

    private final RaceDAO raceDAO;

    public RaceService(RaceDAO raceDAO) {
        this.raceDAO = raceDAO;
    }

    public void saveRace(Race race) {
        raceDAO.add(race);
    }

    public List<Race> findAll() {
        return new ArrayList<>(raceDAO.getAll());
    }

    public Race getRaceById(int id) {
        return raceDAO.findById(id);
    }

    public void updateRace(Race race) {
        raceDAO.update(race, race.getId());
    }

    public void deleteRace(int id) {
        Race race = raceDAO.findById(id);
        if (race != null) {
            raceDAO.delete(race);
        }
    }

    public List<Race> getRacesPage(int pageNumber) {
        return raceDAO.findPage(pageNumber);
    }

    public int getTotalRaces() {
        return raceDAO.getTotalRaces();
    }

    public Race findByDetails(String dest, String date, String time) {
        List<Race> races = new ArrayList<>(raceDAO.getAll());

        for (Race race : races) {
            if (race.getDestination().equalsIgnoreCase(dest)
                    && race.getDate().equalsIgnoreCase(date)
                    && race.getTime().equalsIgnoreCase(time)) {
                return race;
            }
        }

        return null;
    }
}