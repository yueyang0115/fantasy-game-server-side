package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table( name = "Soldier" )
public class Soldier extends Unit{

    //@Column(name = "unit_type", unique = false, nullable = false, length = 100)
    private String u_type = "soldier";

    @Column(name = "soldier_type", unique = false, nullable = false, length = 100)
    private String s_type;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="player_id", nullable=false)
    private Player player;

    public Soldier(){

    }

    public Soldier(String type,int hp,int atk,int speed){
        this.s_type = type;
        this.setHp(hp);
        this.setAtk(atk);
        this.setSpeed(speed);
    }

    public Soldier(Soldier old_soldier){
        this.s_type = old_soldier.getType();
        this.setHp(old_soldier.getHp());
        this.setAtk(old_soldier.getAtk());
        this.setSpeed(old_soldier.getSpeed());
    }

    public String getType() {
        return s_type;
    }

    public void setType(String type) {
        this.s_type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public JSONObject toJSON(){
        JSONObject soldier_obj = new JSONObject();
        //soldier_obj.put("id",id);
        soldier_obj.put("type",s_type);
        //soldier_obj.put("hp",hp);
        //soldier_obj.put("atk",atk);
        return soldier_obj;
    }
}
