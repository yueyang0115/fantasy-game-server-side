package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Unit")
@Inheritance(strategy = InheritanceType.JOINED)
public class Unit {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "unit_type", unique = false, nullable = false, length = 100)
    private String type;

    @Column(name = "HP", unique = false, nullable = false)
    private int hp;

    @Column(name = "ATK", unique = false, nullable = false)
    private int atk;

    @Column(name = "speed", unique = false, nullable = false)
    private int speed;
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
//    private List<ItemPack> equipments = new ArrayList<>();

    public Unit(){}

    public Unit(Unit unit){
        this.id = unit.getId();
        this.type = unit.getType();
        this.hp = unit.getHp();
        this.atk = unit.getAtk();
        this.speed = unit.getSpeed();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void addHp(int heal_hp) {
        hp += heal_hp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

//    public boolean addEquipment(ItemPack equipment) {
//        int ind = equipments.indexOf(equipment);
//        if (ind == -1) {
//            equipments.add(equipment);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public List<ItemPack> getEquipment() {
//        return equipments;
//    }
//
//    public void setEquipment(List<ItemPack> equipment) {
//        this.equipments = equipment;
//    }
}
