package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.json.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Territory")
public class Territory {

    @EmbeddedId
    WorldCoord coord;


    @Column(name = "status", nullable = false)
    private String status;

    /*@JsonManagedReference
      @OneToMany(mappedBy = "territory", cascade = CascadeType.ALL)
      private List<Monster> monsters = new ArrayList<>();
    */
    private String terrainType;

    @JsonManagedReference(value = "territory-building")
    @OneToOne
    @JoinColumn(name = "building_id")
    private Building building;

    public Territory() {

    }

    public Territory(Territory old_terr) {
        this.coord = old_terr.coord;
        this.status = old_terr.getStatus();
        /*List<Monster> monsters = new ArrayList<>();
        for(Monster monster:old_terr.getMonsters()){ // solve lazy initialize problem
            Monster new_monster = new Monster(monster);
            monsters.add(monster);
        }
        this.monsters = monsters;*/
        this.terrainType = old_terr.getTerrainType();
        this.building = old_terr.getBuilding();
    }

    public Territory(WorldCoord coord, String status) {
        this.coord = coord;
        this.status = status;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
        building.setTerritory(this);
    }

    public String getTerrainType() { return terrainType; }

    public void setTerrainType(String terrainType) { this.terrainType = terrainType; }

    public WorldCoord getCoord() { return coord; }

    public void setCoord(WorldCoord coord) { this.coord = coord; }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

  /*public void addMonster(Monster monster) {
      //monster.setTerritory(this);
        this.monsters.add(monster);
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
        }*/

}
