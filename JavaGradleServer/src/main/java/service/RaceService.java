package service;

import model.Race;
import repository.RaceDAO;

import java.util.List;

public class RaceService {

    private final RaceDAO raceDAO;

    public RaceService(RaceDAO raceDAO) {
        this.raceDAO = raceDAO;
    }

    public void saveRace(Race race) {
        raceDAO.save(race);
    }

    public List<Race> findAll() {
        return raceDAO.findAll();
    }

    public Race getRaceById(int id) {
        return raceDAO.findById(id);
    }

    public void updateRace(Race race) {
        raceDAO.update(race);
    }

    public void deleteRace(int id) {
        raceDAO.deleteById(id);
    }

    public List<Race> getRacesPage(int pageNumber) {
        return raceDAO.findPage(pageNumber);
    }

    public int getTotalRaces() {
        return raceDAO.getTotalRaces();
    }
}