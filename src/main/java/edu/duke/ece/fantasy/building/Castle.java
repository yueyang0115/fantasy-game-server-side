package edu.duke.ece.fantasy.building;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.TerritoryDAO;
import org.hibernate.Session;

import java.util.List;

public class Castle extends Building {


    public Castle() {
        super("castle", 1000);
    }

    @Override
    public void onCreate(MetaDAO metaDAO, WorldCoord coord) {
        super.onCreate(metaDAO, coord);
        TerritoryDAO territoryDAO = metaDAO.getTerritoryDAO();
        List<Territory> territories = territoryDAO.getTerritories(coord, 5, 5);
        for (Territory territory : territories) {
            territory.setTame(0);
        }
    }
}