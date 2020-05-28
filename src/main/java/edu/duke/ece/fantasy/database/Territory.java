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

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "WID", nullable = false)
    private int wid;

    @Column(name = "x", nullable = false)
    private int x;

    @Column(name = "y", nullable = false)
    private int y;

    @Column(name = "status", nullable = false)
    private String status;

    @JsonManagedReference
    @OneToMany(mappedBy = "territory", cascade = CascadeType.ALL)
    private List<Monster> monsters = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "terrain_id", nullable = false)
    private Terrain terrain;

    public Territory() {

    }

    public Territory(Territory old_terr) {
        this.wid = old_terr.getWid();
        this.x = old_terr.getX();
        this.y = old_terr.getY();
        this.status = old_terr.getStatus();
        List<Monster> monsters = new ArrayList<>();
        for(Monster monster:old_terr.getMonsters()){ // cut off reference loop
            Monster new_monster = new Monster(monster);
            monsters.add(monster);
        }
        this.monsters = monsters;
        this.terrain = old_terr.getTerrain();
    }

    public Territory(int wid, int x, int y, String status) {
        this.wid = wid;
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        terrain.addTerritory(this);
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addMonster(Monster monster) {
        monster.setTerritory(this);
        this.monsters.add(monster);
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    public JSONObject toJSON() {
        JSONObject territory_obj = new JSONObject();
        territory_obj.put("x", this.x);
        territory_obj.put("y", this.y);
        territory_obj.put("status", this.status);
        territory_obj.put("wid", this.wid);
        JSONArray monster_arr = new JSONArray();
        for (Monster monster : monsters) {
            monster_arr.put(monster.toJSON());
        }
        territory_obj.put("monsters", monster_arr);
        return territory_obj;
    }
}