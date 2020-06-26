package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.duke.ece.fantasy.Item.IItem;
import edu.duke.ece.fantasy.Item.Item;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Player")
public class Player implements Trader {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "money")
    private int money;

//   TODO: private int currentState;

    @Column(name = "moneyGenerationSpeed")
    private int MoneyGenerationSpeed = 0; // TODO:change name

    @Column(name = "WID", columnDefinition = "serial", unique = true, insertable = false, updatable = false, nullable = false)
    private int wid;

    @JsonManagedReference
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<Soldier> soldiers = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<playerInventory> items = new ArrayList<>();

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player() {
    }

    public int getMoneyGenerationSpeed() {
        return MoneyGenerationSpeed;
    }

    public void setMoneyGenerationSpeed(int moneyGenerationSpeed) {
        MoneyGenerationSpeed = moneyGenerationSpeed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(List<Soldier> soldiers) {
        this.soldiers = soldiers;
    }

    public void addSoldier(Soldier soldier) {
        soldier.setPlayer(this);
        this.soldiers.add(soldier);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<playerInventory> getItems() {
        return items;
    }

    public void setItems(List<playerInventory> items) {
        this.items = items;
    }

    public void addItem(playerInventory item) {
        items.add(item);
        item.setPlayer(this);
    }

    private void reduceItem(Inventory inventory, int amount) {
        playerInventory playerInventory = (playerInventory) inventory;
        int left_amount = inventory.getAmount() - amount;
        inventory.setAmount(left_amount);
        if (left_amount == 0) {
            this.getItems().remove(inventory);
            playerInventory.setPlayer(null);
        }
    }

    public void useItem(Inventory inventory, int amount, Unit unit) {
        inventory.getDBItem().toGameItem().OnUse(unit);
        reduceItem(inventory, amount);
    }
//
//    public void dropItem(Inventory inventory, int amount) {
//        reduceItem(inventory, amount);
//    }


    @Override
    public boolean checkMoney(int required_money) {
        return money >= required_money;
    }

    @Override
    public boolean checkItem(Inventory inventory, int amount) {
        for (Inventory item : items) {
            if (item.equals(inventory)) { // if have this type of item
                return item.getAmount() >= amount;
            }
        }
        return false;
    }

    @Override
    public void sellItem(Inventory inventory, int amount) {
        money += amount * inventory.getDBItem().toGameItem().getCost();
        reduceItem(inventory, amount);
    }

    @Override
    public Inventory buyItem(Inventory select_item, int amount) {
        Item item_obj = select_item.getDBItem().toGameItem();
        Inventory record = null;
        for (Inventory item : items) {
            if (item.equals(select_item)) { // if have this type of item, add amount to existing object
                record = item;
                int init_amount = item.getAmount();
                item.setAmount(init_amount + amount);
            }
        }
        money -= amount * item_obj.getCost();
        if (record == null) { // if don't have this type of item, create object
            record = new playerInventory(select_item.getDBItem(), amount, this);
            addItem((playerInventory) record);
        }
        return record;
    }
}
